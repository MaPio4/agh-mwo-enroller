package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

    public Participant findByLogin(String login) {
		String hql = "FROM Participant p WHERE p.login = :login";
		Query query = connector.getSession().createQuery(hql);
		query.setParameter("login", login);
		Collection<Participant> foundParticipants = query.list();

		if(foundParticipants == null || foundParticipants.size() == 0) {
			return null;
		}
		return (Participant) foundParticipants.toArray()[0];
    }

	public boolean addParticipant(Participant participant) {
		try {
			Transaction tr = connector.getSession().getTransaction();
			tr.begin();
			connector.getSession().save(participant);
			tr.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean removeParticipant(Participant participant) {
		try {
			Transaction tr = connector.getSession().getTransaction();
			tr.begin();
			connector.getSession().delete(participant);
			tr.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateParticipant(Participant foundParticipant, Participant participant) {
		if(participant.getPassword() == null) {
			return false;
		}
		try {
			Transaction tr = connector.getSession().getTransaction();
			tr.begin();
			foundParticipant.setPassword(participant.getPassword());
			connector.getSession().save(foundParticipant);
			tr.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
