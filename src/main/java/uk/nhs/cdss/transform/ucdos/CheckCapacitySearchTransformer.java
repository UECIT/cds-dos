package uk.nhs.cdss.transform.ucdos;

import java.util.Date;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uk.nhs.cdss.model.CheckServicesInputBundle;
import uk.nhs.cdss.model.ucdos.SymptomDiscriminator;
import uk.nhs.cdss.model.ucdos.SymptomGroup;
import uk.nhs.cdss.model.ucdos.wsdl.AgeFormatType;
import uk.nhs.cdss.model.ucdos.wsdl.ArrayOfInt;
import uk.nhs.cdss.model.ucdos.wsdl.Case;
import uk.nhs.cdss.model.ucdos.wsdl.CheckCapacitySummary;
import uk.nhs.cdss.model.ucdos.wsdl.GenderType;
import uk.nhs.cdss.model.ucdos.wsdl.UserInfo;

@Component
@AllArgsConstructor
public class CheckCapacitySearchTransformer {

  private AgeGroupTransformer ageGroupTransformer;
  private GenderTransformer genderTransformer;
  private OdsCodeTransformer odsCodeTransformer;

  public CheckCapacitySummary transform(CheckServicesInputBundle inputBundle) {
    var request = new CheckCapacitySummary();

    request.setUserInfo(getUserInfo());
    request.setC(transformCase(inputBundle));

    return request;
  }

  private UserInfo getUserInfo() {
    var userInfoElement = new UserInfo();
    userInfoElement.setUsername("hardcodedUsername");
    userInfoElement.setPassword("hardcodedPassword");
    return userInfoElement;
  }

  private Case transformCase(CheckServicesInputBundle inputBundle) {
    var patient = inputBundle.getPatient();
    var caseElement = new Case();

    caseElement.setCaseId("TEST");
    caseElement.setPostcode(inputBundle.getLocation().getAddress().getPostalCode());
    caseElement.setSurgery(odsCodeTransformer.transform(inputBundle.getRegisteredGp()));
    caseElement.setAge(ageGroupTransformer.transform(patient.getBirthDate()));
    caseElement.setAgeFormat(AgeFormatType.YEARS);
    caseElement.setGender(GenderType.fromValue(genderTransformer.transform(patient.getGender())));
    caseElement.setDisposition(123456789);
    caseElement.setSymptomGroup(SymptomGroup.ACCIDENT_OR_EMERGENCY.getId());
    caseElement.setSymptomDiscriminatorList(new ArrayOfInt());
    caseElement.getSymptomDiscriminatorList().getInt().add(
        SymptomDiscriminator.PCR_WITH_FEATURES_OF_SUBSTANCE_OR_ALCOHOL_MISUSE.getId());
    caseElement.setSearchDistance(inputBundle.getSearchDistance());
    caseElement.setForceSearchDistance(inputBundle.isForceSearchDistance());
    caseElement.setSearchDateTime(new Date().toString());

    return caseElement;
  }

}
