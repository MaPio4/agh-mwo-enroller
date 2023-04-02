package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants() {
		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		if(participant.getLogin() == null || participant.getPassword() == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		if(participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity(HttpStatus.IM_USED);
		}

		if(!participantService.addParticipant(participant)){
			return new ResponseEntity(HttpStatus.CONFLICT);
		};

		if(participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity(HttpStatus.OK);
		}
		else {
			return new ResponseEntity(HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@RequestBody Participant participant) {
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (foundParticipant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		if(!participantService.removeParticipant(foundParticipant)){
			return new ResponseEntity(HttpStatus.CONFLICT);
		};

		if(participantService.findByLogin(participant.getLogin()) == null) {
			return new ResponseEntity(HttpStatus.OK);
		}
		else {
			return new ResponseEntity(HttpStatus.IM_USED);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@RequestBody Participant participant) {
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (foundParticipant == null) {
			return new ResponseEntity("User "+participant.getLogin()+" not found.", HttpStatus.NOT_FOUND);
		}

		if(!participantService.updateParticipant(foundParticipant, participant)){
			return new ResponseEntity("Unable to update "+participant.getLogin()+"'s password." , HttpStatus.CONFLICT);
		};

		return new ResponseEntity("Updated "+participant.getLogin()+"'s password." , HttpStatus.OK);
	}
}
