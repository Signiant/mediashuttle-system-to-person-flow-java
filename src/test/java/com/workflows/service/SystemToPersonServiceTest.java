package com.workflows.service;

import com.workflows.enumeration.WorkflowType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.model.ModelPackage;
import org.openapitools.client.model.PackageTokenRequest;
import org.openapitools.client.model.PackageTokenResponse;
import org.openapitools.client.model.Portal;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SystemToPersonServiceTest {

    @Mock
    private CommonService commonService;

    @InjectMocks
    private SystemToPersonService systemToPersonService;


    private static Stream<Arguments> provideTokenRequestLink() {
        return Stream.of(
                Arguments.of(WorkflowType.FILE_ACQUISITION, PackageTokenRequest.GrantsEnum.UPLOAD, Portal.TypeEnum.SEND),
                Arguments.of(WorkflowType.FILE_ACQUISITION, PackageTokenRequest.GrantsEnum.UPLOAD, Portal.TypeEnum.SUBMIT),
                Arguments.of(WorkflowType.FILE_DISTRIBUTION, PackageTokenRequest.GrantsEnum.DOWNLOAD, Portal.TypeEnum.SHARE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTokenRequestLink")
    public void testCreateTransferLink(WorkflowType workflowType, PackageTokenRequest.GrantsEnum grantsEnum, Portal.TypeEnum type) throws Exception {

        Portal portalsData = new Portal();
        UUID portalId = UUID.randomUUID();
        portalsData.setId(portalId);
        portalsData.setType(type);

        ModelPackage aPackage = new ModelPackage();
        aPackage.setId(UUID.randomUUID().toString());

        PackageTokenRequest packageTokenRequest = new PackageTokenRequest();
        packageTokenRequest.addGrantsItem(grantsEnum);

        PackageTokenResponse expectedTokenResponse = new PackageTokenResponse();
        if(workflowType.equals(WorkflowType.FILE_DISTRIBUTION)){
            expectedTokenResponse.addGrantsItem(PackageTokenResponse.GrantsEnum.DOWNLOAD);
        }else{
            expectedTokenResponse.addGrantsItem(PackageTokenResponse.GrantsEnum.UPLOAD);
        }

        when(commonService.getPortalsData(any(), any())).thenReturn(portalsData);
        when(commonService.createPackage(any())).thenReturn(aPackage);

        when(commonService.createPackageTokenRequest(any(), any(), anyInt(), eq(grantsEnum))).thenReturn(packageTokenRequest);
        when(commonService.createToken(any(), any(), any())).thenReturn(expectedTokenResponse);

        PackageTokenResponse actualTokenResponse = systemToPersonService.createTransferLink(workflowType);
        verify(commonService).getPortalsData(any(), any());
        verify(commonService).createPackage(any());

        if(workflowType.equals(WorkflowType.FILE_DISTRIBUTION)){
            verify(commonService).addPackageFiles(any(), any());
        }

        verify(commonService).createPackageTokenRequest(any(), any(), anyInt(), eq(grantsEnum));
        verify(commonService).createToken(any(), any(), any());

        assertEquals(expectedTokenResponse.getGrants().toString(), actualTokenResponse.getGrants().toString());
    }



}
