package uk.nhs.cdss.resourceProviders;

import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.HealthcareService;
import org.hl7.fhir.dstu3.model.ReferralRequest;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.springframework.stereotype.Component;
import uk.nhs.cdss.service.HealthcareServiceService;

@AllArgsConstructor
@Component
public class HealthcareServiceProvider {

  private HealthcareServiceService healthcareServiceService;

  @Search(type = HealthcareService.class)
  public Bundle searchForHealthcareServices(
      @RequiredParam(name = ReferralRequest.SP_SUBJECT) ReferenceParam referralRequestParam) {
    if (!referralRequestParam.getResourceType().equals(ResourceType.ReferralRequest.name())) {
      throw new InvalidRequestException("Resource type for 'subject' must be 'ReferralRequest'");
    }

    Bundle bundle = new Bundle();
    healthcareServiceService.getAll().stream()
      .map(service -> new BundleEntryComponent().setResource(service))
      .forEach(bundle::addEntry);
    return bundle;
  }
}
