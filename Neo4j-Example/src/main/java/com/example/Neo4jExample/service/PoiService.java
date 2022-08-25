package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PoiService {
    private final PointOfIntRepository pointOfIntRepository;
    private final PoiRequestRepository poiRequestRepository;
    private final UtilityService utilityService;

    private final CityRepository cityRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final CoordinateRepository coordinateRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final AddressRepository addressRepository;
    private final ContactRepository contactRepository;
    private final TagRepository tagRepository;

    public void savePoi(PointOfInterestNode toSave){
        this.pointOfIntRepository.save(toSave);
    }

    public void savePoi(Collection<PointOfInterestNode> toSave){
        toSave.forEach(this::savePoi);
    }

    public boolean poiIsContainedInCity(PointOfInterestNode isContained, CityNode from){
        return from.getPointOfInterests().contains(isContained);
    }

    public void savePoiCity(PointOfInterestNode containedInCity){
        this.cityRepository.save(this.utilityService.getCityOfPoi(containedInCity.getId()));
        log.info("City of Poi {} saved successfully",containedInCity.getName());
    }

    private void deleteTimeSlot(TimeSlot toDelete) {
        this.timeSlotRepository.delete(toDelete);
    }
    private void deleteContact(Contact toDelete) {
        this.contactRepository.delete(toDelete);
    }
    private void deleteAddress(Address toDelete) {
        this.addressRepository.delete(toDelete);
    }
    private void deleteCoordinate(Coordinate toDelete) {
        this.coordinateRepository.delete(toDelete);
    }

    public void deletePoi(PointOfInterestNode toDelete) throws NullPointerException{
        if(Objects.isNull(toDelete)) throw new NullPointerException("poi not available");
        log.info("toDelete {}",toDelete);
        CityNode cityToSave = this.utilityService.getCityOfPoi(toDelete.getId());
        cityToSave.getPointOfInterests().remove(toDelete);
        this.cityRepository.save(cityToSave);
        log.info("citta salvata");
        log.info("coordinate id {}",toDelete.getCoordinate().getId());
        log.info("timeslot id {}",toDelete.getHours().toString());
        log.info("Address id {}",toDelete.getAddress().toString());
        log.info("contact id {}",toDelete.getContact().toString());
        this.deleteCoordinate(toDelete.getCoordinate());
        log.info("cordinate");
        this.deleteTimeSlot(toDelete.getHours());
        log.info("timeslot");
        this.deleteAddress(toDelete.getAddress());
        log.info("address");
        this.deleteContact(toDelete.getContact());
        log.info("contact");
        this.pointOfIntRepository.delete(toDelete);
        log.info("eliminato");
    }

    public PointOfInterestNode findPoiById(Long id){
        return this.pointOfIntRepository.findById(id).orElse(null);
    }

    /**
     * save a PointOfInterest in a City
     * @param where city to insert poi
     * @param toSave poi to save
     */
    public void savePoiInACity(CityNode where,PointOfInterestNode toSave){
        where.getPointOfInterests().add(toSave);
        this.cityRepository.save(where);
    }

/*    private void clearTimeSlot(TimeSlot timeToClear) {
        timeToClear.setMonday(new ArrayList<>());
        timeToClear.setTuesday(new ArrayList<>());
        timeToClear.setWednesday(new ArrayList<>());
        timeToClear.setThursday(new ArrayList<>());
        timeToClear.setFriday(new ArrayList<>());
        timeToClear.setSaturday(new ArrayList<>());
        timeToClear.setSunday(new ArrayList<>());
        this.timeSlotRepository.save(timeToClear);
    }*/

    private TimeSlot copyTimeSlot(TimeSlot toCopy){
        TimeSlot timeSlot = new TimeSlot(toCopy.getMonday(),toCopy.getTuesday(),toCopy.getWednesday(),toCopy.getThursday(),
                toCopy.getFriday(),toCopy.getSaturday(),toCopy.getSunday());
        this.timeSlotRepository.save(timeSlot);
        return timeSlot;
    }

    private Collection<PoiTagRel> copyPoiTagRel(Collection<PoiTagRel> toCopy){
        Collection<PoiTagRel> results = new ArrayList<>();
        toCopy.forEach(value->{
            PoiTagRel poiTagRel = new PoiTagRel(value.getTag());
            if(value.getTag().getIsBooleanType())
                poiTagRel.setBooleanValue(value.getBooleanValue());
            else poiTagRel.setStringValue(value.getStringValue());
            results.add(poiTagRel);
        });
        return results;
    }

    private Address copyAddress(Address toCopy){
        Address result = new Address(toCopy.getStreet(), toCopy.getNumber());
        this.addressRepository.save(result);
        return result;
    }

    private Coordinate copyCoordinate(Coordinate toCopy){
        Coordinate result = new Coordinate(toCopy.getLat(), toCopy.getLon());
        this.coordinateRepository.save(result);
        return result;
    }

    private Contact copyContact(Contact toCopy){
        Contact result = new Contact(toCopy.getEmail(),toCopy.getCellNumber(),toCopy.getFax());
        this.contactRepository.save(result);
        return result;
    }

    /**
     * Create a Poi from a Poi Request
     * @param request from get paramaeters to create poi
     * @return created poi
     */
    public PointOfInterestNode createPoiFromRequest(PoiRequestNode request){
        PointOfInterestNode result = new PointOfInterestNode(request.getName(),request.getDescription(),
                this.copyCoordinate(request.getCoordinate()),this.copyTimeSlot(request.getHours()),
                request.getTimeToVisit(),this.copyAddress(request.getAddress()),request.getTicketPrice(),
                request.getLink(),request.getTypes(),this.copyContact(request.getContact()),
                this.copyPoiTagRel(request.getTagValues()));
        this.pointOfIntRepository.save(result);
        this.savePoiInACity(request.getCity(),result);
        return result;
    }

    /**
     * modifies Poi parameters from a request
     * @param request to get modifies
     */
    //FIXME: rivedere metodo perche crea nuovi nodi
    public void modifyPoiFromRequest(PoiRequestNode request){
        PointOfInterestNode result = request.getPointOfInterestNode();
        result.getHours().setMonday(request.getHours().getMonday());
        result.getHours().setTuesday(request.getHours().getTuesday());
        result.getHours().setWednesday(request.getHours().getWednesday());
        result.getHours().setThursday(request.getHours().getThursday());
        result.getHours().setFriday(request.getHours().getFriday());
        result.getHours().setSaturday(request.getHours().getSaturday());
        result.getHours().setSunday(request.getHours().getSunday());
        this.timeSlotRepository.save(result.getHours());
        result.getCoordinate().setLat(request.getCoordinate().getLat());
        result.getCoordinate().setLon(request.getCoordinate().getLon());
        this.coordinateRepository.save(result.getCoordinate());
        result.setTicketPrice(request.getTicketPrice());
        result.getContact().setEmail(request.getContact().getEmail());
        result.getContact().setCellNumber(request.getContact().getCellNumber());
        result.getContact().setFax(request.getContact().getFax());
        this.contactRepository.save(result.getContact());
        result.setName(request.getName());
        result.setDescription(request.getDescription());
        result.setTimeToVisit(request.getTimeToVisit());
        result.getAddress().setStreet(request.getAddress().getStreet());
        result.getAddress().setNumber(request.getAddress().getNumber());
        this.addressRepository.save(result.getAddress());
        result.getContributors().add(request.getUsername());
        result.setLink(request.getLink());
        result.setTypes(request.getTypes());
        result.setTagValues(request.getTagValues());
        this.pointOfIntRepository.save(result);
        this.poiRequestRepository.save(request);
        this.savePoiCity(result);
    }

    /**
     * create a PointOfInterestNode from a body request
     *
     * @param bodyFrom body request
     * @return PointOfInterestNode just created
     */
    //TODO:stiamo modificando
    public PointOfInterestNode createPoiFromBody(Map<String, Object> bodyFrom) {
        PointOfInterestNode result = new PointOfInterestNode();
        String username = (String) bodyFrom.get("username");
        if (!Objects.isNull(username)) result.getContributors().add(username);
        String name = (String) bodyFrom.get("name");
        result.setName(name);
        String description = (String) bodyFrom.get("description");
        result.setDescription(description);
        Coordinate coordinate = this.utilityService.createCoordsFromString(
                (String) bodyFrom.get("lat"), (String) bodyFrom.get("lon"));
        log.info("Coordinate created : {}",coordinate.toString());
        result.setCoordinate(coordinate);
        String street = (String) bodyFrom.get("street");
        Integer number = Integer.parseInt((String) bodyFrom.get("number"));
        Address address = this.utilityService.createAddress(street, number);
        log.info("Address created : {}",address.toString());
        result.setAddress(address);
        Contact contact = this.utilityService.createContact((String) bodyFrom.get("email"), (String) bodyFrom.get("phone"),
                (String) bodyFrom.get("fax"));
        log.info("Contact created : {}",contact.toString());
        result.setContact(contact);
        Double timeToVisit = Double.parseDouble((String) bodyFrom.get("timeToVisit"));
        result.setTimeToVisit(timeToVisit);
        Double ticketPrice = Double.parseDouble((String) bodyFrom.get("price"));
        result.setTicketPrice(ticketPrice);
        TimeSlot timeSlot = this.utilityService.getTimeSlotFromBody(new TimeSlot(),bodyFrom);
        result.setHours(timeSlot);
        Collection<PoiType> poiTypes = ((Collection<String>) bodyFrom.get("types")).stream()
                .filter(a -> poiTypeRepository.findById(a).isPresent())
                .map(a -> poiTypeRepository.findById(a).get())
                .collect(Collectors.toList());
        result.setTypes(poiTypes);
        result.getTagValues().addAll(this.utilityService.createPoiTagRel((Collection<Map<String, Object>>) bodyFrom.get("tags")));
        this.pointOfIntRepository.save(result);
        return result;
    }

    private void clearTimeSlot(TimeSlot toClear){
        toClear.getMonday().clear();
        toClear.getTuesday().clear();
        toClear.getWednesday().clear();
        toClear.getTuesday().clear();
        toClear.getFriday().clear();
        toClear.getSaturday().clear();
        toClear.getSunday().clear();
    }

    /**
     * modify a PointOfInterestNode with params contained in a body request
     *
     * @param poiToModify PointOfInterestNode to modify
     * @param bodyFrom    body request contained values to set
     */
    //TODO: modificare solo quello che cambia dal body
    public void modifyPoiFromBody(PointOfInterestNode poiToModify, Map<String, Object> bodyFrom) {
        String username = (String) bodyFrom.get("username");
        if(!Objects.isNull(username)) poiToModify.getContributors().add(username);
        String name = this.utilityService.getValueFromBody("name",bodyFrom);
        poiToModify.setName(name);
        String description = this.utilityService.getValueFromBody("description",bodyFrom);
        poiToModify.setDescription(description);
        poiToModify.getCoordinate().setLat(Double.parseDouble(this.utilityService.getValueFromBody("lat",bodyFrom)));
        poiToModify.getCoordinate().setLon(Double.parseDouble(this.utilityService.getValueFromBody("lon",bodyFrom)));
        this.coordinateRepository.save(poiToModify.getCoordinate());
        poiToModify.getAddress().setStreet(this.utilityService.getValueFromBody("street",bodyFrom));
        poiToModify.getAddress().setNumber(Integer.parseInt(this.utilityService.getValueFromBody("number",bodyFrom)));
        this.addressRepository.save(poiToModify.getAddress());
        poiToModify.getContact().setEmail(this.utilityService.getValueFromBody("email",bodyFrom));
        poiToModify.getContact().setCellNumber(this.utilityService.getValueFromBody("phone",bodyFrom));
        poiToModify.getContact().setFax(this.utilityService.getValueFromBody("fax",bodyFrom));
        this.contactRepository.save(poiToModify.getContact());
        poiToModify.setTimeToVisit(Double.parseDouble(this.utilityService.getValueFromBody("timeToVisit",bodyFrom)));
        poiToModify.setTicketPrice(Double.parseDouble(this.utilityService.getValueFromBody("price",bodyFrom)));
        this.clearTimeSlot(poiToModify.getHours());
        this.utilityService.getTimeSlotFromBody(poiToModify.getHours(),bodyFrom);
        Collection<PoiType> poiTypes = ((Collection<String>) bodyFrom.get("types")).stream()
                .filter(a -> this.poiTypeRepository.findById(a).isPresent())
                .map(a -> this.poiTypeRepository.findById(a).get())
                .collect(Collectors.toList());
        poiToModify.setTypes(poiTypes);
        poiToModify.getTagValues().clear();
        poiToModify.getTagValues().addAll(this.utilityService
                .createPoiTagRel((Collection<Map<String, Object>>) bodyFrom.get("tags")));
        this.pointOfIntRepository.save(poiToModify);

    }
}
