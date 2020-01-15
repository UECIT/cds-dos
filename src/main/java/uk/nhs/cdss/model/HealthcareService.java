package uk.nhs.cdss.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HealthcareService {

  private long id;
  private List<Endpoint> endpoints;
  private boolean active;
  private String name;
  private boolean appointmentRequired;
  private List<String> serviceTypes = new ArrayList<>();
  private List<String> serviceSpecialties = new ArrayList<>();
  private Address address;
  private String description;
  private String extraDetails;
  private List<ContactPoint> contacts = new ArrayList<>();
  private List<String> provisions = new ArrayList<>();
  private List<AvailableTime> availableTimes = new ArrayList<>();
  private List<UnavailableTime> unavailableTimes = new ArrayList<>();

}
