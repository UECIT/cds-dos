package uk.nhs.cdss.transform;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Transformer;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.springframework.stereotype.Component;
import uk.nhs.cdss.model.HealthcareService;

@Component
@AllArgsConstructor
public class HealthcareServiceTransformer implements Transformer<HealthcareService, org.hl7.fhir.dstu3.model.HealthcareService> {

  private EndpointTransformer endpointTransformer;

  @Override
  public org.hl7.fhir.dstu3.model.HealthcareService transform(HealthcareService from) {
    org.hl7.fhir.dstu3.model.HealthcareService healthcareService =
        new org.hl7.fhir.dstu3.model.HealthcareService();

    healthcareService.setId(String.valueOf(from.getId()));
    healthcareService.setActive(from.isActive())
        .setName(from.getName())
        .setAppointmentRequired(from.isAppointmentRequired());

    List<Resource> endpoints = from.getEndpoints().stream()
        .map(endpointTransformer::transform)
        .collect(Collectors.toList());

    healthcareService.setContained(endpoints);

    List<Reference> endpointReferences = endpoints.stream()
        .map(Reference::new)
        .collect(Collectors.toList());

    healthcareService.setEndpoint(endpointReferences);

    return healthcareService;
  }
}
