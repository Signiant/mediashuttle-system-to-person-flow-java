package com.workflows.service;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.api.PortalsApi;
import org.openapitools.client.api.SystemToPersonApi;
import org.openapitools.client.auth.ApiKeyAuth;
import org.openapitools.client.model.FileRequest;
import org.openapitools.client.model.PackageTokenResponse;
import org.openapitools.client.model.PackageTokenRequest;
import org.openapitools.client.model.Portal;
import org.openapitools.client.model.PackageTokenRequestNotificationsInner;
import org.openapitools.client.model.PackageTokenRequestUser;
import org.openapitools.client.model.FileSetRequest;
import org.openapitools.client.model.FileSetResponse;
import org.openapitools.client.model.ModelPackage;
import org.openapitools.client.model.PortalList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class CommonService{

    @Autowired
    private FileRequestPropsService fileRequestPropsService;

    @Value("${base.path}")
    private String basePath;

    @Value("${user.mail}")
    private String userMail;

    @Value("${destination.path:#{null}}")
    private String destinationPath;

    SystemToPersonApi systemToPersonApi = new SystemToPersonApi();

    public void setAuthentication(String devKey){
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath(basePath);
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("ApiKey");
        apiKey.setApiKey(devKey);
    }

    /*
        Returns the Portals details data
    */
    public Portal getPortalsData(String portalUrl, PortalsApi portalsApi) throws ApiException {
        PortalList portalList = portalsApi.listPortals(portalUrl);
        if (!portalList.getItems().isEmpty()) {
            return portalList.getItems().get(0);
        } else {
            throw new RuntimeException("No portals found");
        }
    }

    public ModelPackage createPackage(UUID portalId) throws ApiException {
        return systemToPersonApi.createPackage(portalId);
    }

    public PackageTokenResponse createToken(UUID portalId, String packageId, PackageTokenRequest packageTokenRequest) throws ApiException {
        return systemToPersonApi.createToken(portalId, packageId, packageTokenRequest);
    }

    public PackageTokenRequest createPackageTokenRequest(String type, String webhookUrl, int expiryDay, PackageTokenRequest.GrantsEnum grantsEnum){

        PackageTokenRequest packageTokenRequest = new PackageTokenRequest()
                .user(new PackageTokenRequestUser().email(userMail))
                .addGrantsItem(grantsEnum)
                .expiresOn(OffsetDateTime.now().plusDays(expiryDay))
                .notifications(Collections.singletonList(new PackageTokenRequestNotificationsInner()
                        .url(webhookUrl)
                        .type("webhook")));

        if (Objects.nonNull(destinationPath) && !destinationPath.isEmpty() && !type.equalsIgnoreCase("Send")) {
            packageTokenRequest.destinationPath(destinationPath);
        }

        return packageTokenRequest;
    }

    public FileSetResponse addPackageFiles(UUID portalId, String packageId) throws ApiException {
        List<FileRequest> fileRequestList = fileRequestPropsService.getFiles();
        if (!fileRequestList.isEmpty()){
            FileSetRequest files = new FileSetRequest().files(fileRequestList);
            return systemToPersonApi.putPackages(portalId, packageId, files);
        }else{
            throw new RuntimeException("No files found");
        }

    }

}
