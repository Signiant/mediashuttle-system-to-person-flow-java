package com.workflows.controller;

import com.workflows.service.SystemToPersonService;
import com.workflows.enumeration.WorkflowType;
import org.openapitools.client.model.PackageTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemToPersonController {

    @Autowired
    private SystemToPersonService systemToPersonService;


    @GetMapping("/fileAcquisition")
    public PackageTokenResponse createFileAcquisitionLink(){
        return systemToPersonService.createTransferLink(WorkflowType.FILE_ACQUISITION);
    }

    @GetMapping("/fileDistribution")
    public PackageTokenResponse createFileDistributionLink(){
        return systemToPersonService.createTransferLink(WorkflowType.FILE_DISTRIBUTION);
    }
}
