package uk.nhs.cdss.transform.ucdos;

import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.springframework.stereotype.Component;

@Component
public class GenderTransformer {

  public String transform(AdministrativeGender gender) {
    switch (gender) {
      case MALE:
        return "M";
      case FEMALE:
        return "F";
      default:
        return "I";
    }
  }

}
