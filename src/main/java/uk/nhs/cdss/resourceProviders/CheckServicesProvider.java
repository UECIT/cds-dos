package uk.nhs.cdss.resourceProviders;

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.param.NumberParam;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
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
  public Parameters searchForHealthcareServices(
      @OperationParam(name = "referralRequest", min = 1, max = 1) ReferralRequest referralRequest,
      @OperationParam(name = "patient", min = 1, max = 1) Patient patient,
      @OperationParam(name = "requestId", max = 1) IdType requestId,
      @OperationParam(name = "location", max = 1) Location location,
      @OperationParam(name = "requester", max = 1) IBaseResource requester,
      @OperationParam(name = "searchDistance", max = 1) NumberParam searchDistance,
      @OperationParam(name = "registeredGP", max = 1) Organization registeredGp,
      @OperationParam(name = "inputParameters", max = 1) Parameters inputParameters
  ) {
    String errorMessage = "%s is required";
    Preconditions.checkNotNull(referralRequest, errorMessage, "referralRequest");
    Preconditions.checkNotNull(patient, errorMessage, "patient");

    boolean forceSearchDistance = inputParameters.getParameter()
        .stream()
        .filter(p -> "forceSearchDistance".equals(p.getName()))
        .findFirst()
        .map(ParametersParameterComponent::getValue)
        .map(BooleanType.class::cast)
        .map(BooleanType::booleanValue)
        .orElse(false);

    var input = CheckServicesInputBundle.builder()
        .referralRequest(referralRequest)
        .patient(patient)
        .requestId(requestId != null ? requestId.getValue() : null)
        .location(location)
        .requester(requester)
        .searchDistance(searchDistance != null ? searchDistance.getValue().intValue() : null)
        .registeredGp(registeredGp)
        .forceSearchDistance(forceSearchDistance)
        .build();

    ucdosService.invokeUCDOS(input);

    var returnedServices = new Bundle();
    healthcareServiceService.getAll().stream()
        .map(service -> new BundleEntryComponent().setResource(service))
        .forEach(returnedServices::addEntry);

    var outputParameters = new Parameters();

    var wrappingParameters = new Parameters();
    wrappingParameters.addParameter().setName("return").setResource(returnedServices);
    wrappingParameters.addParameter().setName("outputParameters").setResource(outputParameters);

    return wrappingParameters;
  }
}
