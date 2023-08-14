package com.workflow.service;

import com.workflows.service.CommonService;
import com.workflows.service.FileRequestPropsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PortalsApi;
import org.openapitools.client.api.SystemToPersonApi;
import org.openapitools.client.model.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommonServiceTest {
    @InjectMocks
    private CommonService commonService;

    @Mock
    private SystemToPersonApi sysToPersonApi;

    @Mock
    private FileRequestPropsService fileRequestPropsService;

    private String user_mail = "user@example.com";
    private String destination_path = "destination/path";
    private String webhook_url = "https://example.com/webhook";
    private String portal_url = "test-mediashuttle.com";
    private String packageId = "2a81ee65e73ab3fcc62c";

    @BeforeEach
    public void setup() throws Exception {
        setFieldValue(commonService, "destinationPath", "destination/path");
        setFieldValue(commonService, "userMail", "user@example.com");
    }

    private void setFieldValue(Object targetObject, String fieldName, Object fieldValue) throws Exception {
        Field field = targetObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(targetObject, fieldValue);
    }

    @Test
    public void addPackageFiles_success() throws ApiException {
        UUID portalId = UUID.randomUUID();

        List<FileRequest> fileRequestList = Collections.singletonList(new FileRequest()
                .path("/test")
                .isDirectory(false));
        FileRequestPropsService fileRequestObj = new FileRequestPropsService();
        fileRequestObj.setFiles(fileRequestList);
        when(fileRequestPropsService.getFiles()).thenReturn(fileRequestObj.getFiles());

        List<FileResponse> fileResponses = Collections.singletonList(new FileResponse()
                .path("/test")
                .isDirectory(false));

        FileSetResponse expectedResult = new FileSetResponse();
        expectedResult.setFiles(fileResponses);

        when(sysToPersonApi.putPackages(any(), any(), any(FileSetRequest.class))).thenReturn(expectedResult);
        FileSetResponse actualResult = commonService.addPackageFiles(portalId, packageId);
        assertEquals(expectedResult, actualResult);
        verify(sysToPersonApi, times(1)).putPackages(any(), any(), any(FileSetRequest.class));
    }


    @Test
    public void addPackageFiles_throwsExceptionIfNoPackageFilesPresent() throws ApiException {
        UUID portalId = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> {
            commonService.addPackageFiles(portalId, packageId);
        });
        verify(sysToPersonApi, times(0))
                .putPackages(any(), any(), any(FileSetRequest.class));
    }


    @Test
    public void getPortalsData_success() throws ApiException {
        PortalList portalList = new PortalList();
        Portal portal = new Portal();
        portal.setId(UUID.randomUUID());
        portalList.setItems(Collections.singletonList(portal));

        PortalsApi portalsApi = Mockito.mock(PortalsApi.class);
        Mockito.when(portalsApi.listPortals(portal_url)).thenReturn(portalList);

        Portal result = commonService.getPortalsData(portal_url, portalsApi);

        Assertions.assertNotNull(result);
        assertEquals(result.getId(), portalList.getItems().get(0).getId());
    }


    @Test
    public void getPortalsData_throwsExceptionIfNoPortalsFound() throws ApiException {
        PortalList portalList = new PortalList();
        portalList.setItems(Collections.emptyList());
        PortalsApi portalsApi = Mockito.mock(PortalsApi.class);

        when(portalsApi.listPortals(any())).thenReturn(portalList);

        Assertions.assertThrows(RuntimeException.class, () -> {
            commonService.getPortalsData(portal_url, portalsApi);
        }, "No portals found")  ;

        verify(portalsApi, times(1)).listPortals(any());
    }


    @Test
    public void createPackageTokenRequest_success() {
        String type = "Share";
        int expiryDay = 7;
        PackageTokenRequest.GrantsEnum grantsEnum = PackageTokenRequest.GrantsEnum.DOWNLOAD;

        PackageTokenRequest result = commonService.createPackageTokenRequest(type, webhook_url, expiryDay, grantsEnum);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(grantsEnum, result.getGrants().get(0));
        Assertions.assertEquals(user_mail, result.getUser().getEmail());
        Assertions.assertEquals(destination_path, result.getDestinationPath());
    }

}
