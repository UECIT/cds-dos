package uk.nhs.cdss.resourceProviders;

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.param.NumberParam;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.ReferralRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.stereotype.Component;
import uk.nhs.cdss.model.CheckServicesInputBundle;
import uk.nhs.cdss.service.HealthcareServiceService;
import uk.nhs.cdss.service.UCDOSService;

@AllArgsConstructor
@Component
public class CheckServicesProvider {

  private HealthcareServiceService healthcareServiceService;
  private UCDOSService ucdosService;

  @Operation(name = "$check-services")
  public Bundle searchForHealthcareServices(
      @OperationParam(name = "referralRequest", min = 1, max = 1) ReferralRequest referralRequest,
      @OperationParam(name = "patient", min = 1, max = 1) Patient patient,
      @OperationParam(name = "requestId", max = 1) IdType requestId,
      @OperationParam(name = "location", max = 1) Location location,
      @OperationParam(name = "requester", max = 1) IBaseResource requester,
      @OperationParam(name = "searchDistance", max = 1) NumberParam searchDistance
  ) {
    String errorMessage = "%s is required";
    Preconditions.checkNotNull(referralRequest, errorMessage, "referralRequest");
    Preconditions.checkNotNull(patient, errorMessage, "patient");

    CheckServicesInputBundle input = CheckServicesInputBundle.builder()
        .referralRequest(referralRequest)
        .patient(patient)
        .requestId(requestId != null ? requestId.getValue() : null)
        .location(location)
        .requester(requester)
        .searchDistance(searchDistance != null ? searchDistance.getValue().intValue() : null)
        .build();

    ucdosService.invokeUCDOS(input);

    Bundle bundle = new Bundle();
    healthcareServiceService.getAll().stream()
        .map(service -> new BundleEntryComponent().setResource(service))
        .forEach(bundle::addEntry);
    return bundle;
  }
}
