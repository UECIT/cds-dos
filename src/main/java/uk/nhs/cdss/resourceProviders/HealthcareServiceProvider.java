package uk.nhs.cdss.resourceProviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.HealthcareService;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.ReferralRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import uk.nhs.cdss.service.HealthcareServiceService;

@AllArgsConstructor
@Component
public class HealthcareServiceProvider implements IResourceProvider {

  private HealthcareServiceService healthcareServiceService;

  @Read
  public HealthcareService get(@IdParam IdType id) {
    return healthcareServiceService.getAll().stream()
        .filter(hcs -> hcs.getId().equals(id.getIdPart()))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException(id));
  }

  @Operation(name = "$check-services", type = HealthcareService.class)
  public Bundle searchForHealthcareServices(
      @OperationParam(name = "referralRequest") ReferralRequest referralRequest) {

    Bundle bundle = new Bundle();
    healthcareServiceService.getAll().stream()
        .map(service -> new BundleEntryComponent().setResource(service))
        .forEach(bundle::addEntry);
    return bundle;
  }

  @Override
  public Class<? extends IBaseResource> getResourceType() {
    return HealthcareService.class;
  }
}
