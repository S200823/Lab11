package it.polito.tdp.rivers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import it.polito.tdp.rivers.db.RiversDAO;

public class Core {

	private RiversDAO dao;
	private List<Flow> flows;
	private List<River> rivers;
	private Queue<Event> events;

	public Core() {
		dao = new RiversDAO();
		flows = new ArrayList<Flow>();
		rivers = new ArrayList<River>();
	}

	public List<River> getAllRivers() {
		rivers = dao.getAllRivers();
		return rivers;
	}

	public void getAllFlows() {
		flows = dao.getAllFlows(rivers);
		// Metti tutti i flow per ogni river
		for (Flow f : flows)
			f.getRiver().addFlow(f);

		System.out.println(rivers.get(0).getFlows());
	}

	public String getFirstFlowOfRiver(River river) {
		return river.getFlows().get(0).getDay().toString();
	}

	public String getLastFlowOfRiver(River river) {
		return river.getFlows().get(river.getFlows().size() - 1).getDay().toString();
	}

	public String getNumberOfMeasurementsOfRiver(River river) {
		return "" + river.getFlows().size();
	}

	public double getFMedOfRiver(River river) {
		double fTot = 0.0;
		for (Flow f : river.getFlows())
			fTot += f.getFlow();
		double fMed = fTot / river.getFlows().size();
		return fMed;
	}

	public String simula(River river, int k) {
		events = new PriorityQueue<Event>();
		Random r = new Random();
		// Q = k * fmed del fiume * secondi * minuti * ore / giorno * 30 giorni.
		double Q = k * getFMedOfRiver(river) * 60 * 60 * 24 * 30;
		double C = Q / 2;
		double Cmed = 0;
		for (Flow f : river.getFlows()) {
			events.add(new Event(f.getDay(), f.getFlow() * 60 * 60 * 24, Event.EventType.ENTRA_ACQUA));
		}
		String textOutPut = "**********INIZIO SIMULAZIONE**********";
		textOutPut += "\nCapacità bacino: " + Q + " *** Bacino all'inizio: " + C + ".";
		while (!events.isEmpty()) {
			Event event = events.remove();
			switch (event.getEvent()) {
			case ENTRA_ACQUA:
				C += event.getFlow();
				textOutPut += "\n\t" + event.getDay() + " - Entra flusso d'acqua di " + event.getFlow()
						+ " m^3.\n\tBacino: " + C + ".";
				if (C < Q) {
					boolean irrigazione = r.nextFloat() <= 0.05;
					if (!irrigazione)
						events.add(new Event(event.getDay(), (r.nextDouble() + 1) * event.getFlow() * 0.8,
								Event.EventType.ESCE_ACQUA));
					else
						events.add(new Event(event.getDay(), 10 * event.getFlow() * 0.8,
								Event.EventType.ESCE_ACQUA_PER_IRRIGARE_CAMPI));
					Cmed += C;
				} else {
					events.add(new Event(event.getDay(), C - Q, Event.EventType.TRACIMAZIONE));
					Cmed += Q;
				}
				break;
			case ESCE_ACQUA:
				if (C - event.getFlow() < 0) {
					events.add(new Event(event.getDay(), C, Event.EventType.BACINO_VUOTO));
				} else {
					C -= event.getFlow();
					textOutPut += "\n\t" + event.getDay() + " - Esce flusso d'acqua di " + event.getFlow()
							+ " m^3.\n\tBacino: " + C + ".";
				}
				break;
			case BACINO_VUOTO:
				textOutPut += "\n\t" + event.getDay() + " - Esce flusso d'acqua di " + event.getFlow()
						+ " m^3.\n\tBacino vuoto.";
				C = 0;
				break;
			case TRACIMAZIONE:
				textOutPut += "\n\t" + event.getDay() + " - Tracimazione di " + event.getFlow() + " m^3.\n\tBacino: "
						+ Q + ".";
				C = Q;
				break;
			case ESCE_ACQUA_PER_IRRIGARE_CAMPI:
				if (C - event.getFlow() < 0) {
					events.add(new Event(event.getDay(), C, Event.EventType.BACINO_VUOTO));
				} else {
					C -= event.getFlow();
					textOutPut += "\n\t" + event.getDay() + " - Esce flusso d'acqua per irrigare i campi di "
							+ event.getFlow() + " m^3.\n\tBacino: " + C + ".";
				}
				break;
			default:
				System.err.println("PANICO!");
				break;
			}
		}
		textOutPut += "\n**********FINE SIMULAZIONE**********";
		textOutPut += String.format("\n\tOccupazione media del bacino: %.2f%%",
				Cmed / (Q * river.getFlows().size()) * 100);
		return textOutPut;
	}

}
