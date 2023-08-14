package com.workflows.service;

import com.workflows.enumeration.WorkflowType;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PortalsApi;
import org.openapitools.client.api.SystemToPersonApi;
import org.openapitools.client.model.ModelPackage;
import org.openapitools.client.model.PackageTokenRequest;
import org.openapitools.client.model.PackageTokenResponse;
import org.openapitools.client.model.Portal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class SystemToPersonService {
    @Autowired
    CommonService common;

    @Value("${portal.url}")
    String portalUrl;

    @Value("${webhook.url}")
    String webhookUrl;

    @Value("${tokenUrl.expiryDay}")
    int expiryDay;

    public PackageTokenResponse createTransferLink(WorkflowType workflowType) {
        try {
            PortalsApi portalsApi = new PortalsApi();

            Portal portalsData = common.getPortalsData(portalUrl, portalsApi);
            UUID portalId = portalsData.getId();
            String type = portalsData.getType().toString();

            ModelPackage aPackage = common.createPackage(portalId);
            PackageTokenRequest.GrantsEnum gransEnum = PackageTokenRequest.GrantsEnum.UPLOAD;

            if (workflowType.equals(WorkflowType.FILE_DISTRIBUTION)) {
                common.addPackageFiles(portalId, aPackage.getId());
                gransEnum =  PackageTokenRequest.GrantsEnum.DOWNLOAD;
            }

            PackageTokenRequest packageTokenRequest = common.createPackageTokenRequest(type, webhookUrl, expiryDay, gransEnum);

            return common.createToken(portalId, aPackage.getId(), packageTokenRequest);

        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}
