package EmployeeManagementSystem.service;

import EmployeeManagementSystem.entity.OfficeLocation;
import EmployeeManagementSystem.repository.OfficeLocationRepository;
import EmployeeManagementSystem.utils.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeofenceService {

    private final OfficeLocationRepository officeLocationRepository;


    private OfficeLocation getOfficeLocation() {

        return officeLocationRepository.findAll()
                .stream()
                .findFirst()
                .orElseGet(() -> {

                    OfficeLocation office = new OfficeLocation();


                    // Change these values according to your office location
                    office.setOfficeName("Main Office");
                    office.setLatitude(28.613939);
                    office.setLongitude(77.209021);
                    office.setAllowedRadius(200.0);

                    return officeLocationRepository.save(office);
                });
    }


     // Returns true if employee is inside office.

    public boolean isInsideOffice(Double userLatitude,
                                  Double userLongitude) {

        OfficeLocation office = getOfficeLocation();

        double distance = DistanceCalculator.calculateDistance(
                userLatitude,
                userLongitude,
                office.getLatitude(),
                office.getLongitude());

        return distance <= office.getAllowedRadius();
    }


//      Returns employee distance from office.

    public double getDistance(Double userLatitude,
                              Double userLongitude) {

        OfficeLocation office = getOfficeLocation();

        return DistanceCalculator.calculateDistance(
                userLatitude,
                userLongitude,
                office.getLatitude(),
                office.getLongitude());
    }


//      Returns office details.
    public OfficeLocation getOffice() {
        return getOfficeLocation();
    }
}