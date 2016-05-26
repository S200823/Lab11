package it.polito.tdp.rivers.model;

import java.time.LocalDate;

public class Event implements Comparable<Event> {

	public enum EventType {
		ENTRA_ACQUA, ESCE_ACQUA, TRACIMAZIONE, BACINO_VUOTO, ESCE_ACQUA_PER_IRRIGARE_CAMPI;
	}

	private LocalDate day;
	private double flow;
	private EventType event;

	public Event(LocalDate day, double flow, EventType event) {
		this.day = day;
		this.flow = flow;
		this.event = event;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public double getFlow() {
		return flow;
	}

	public void setFlow(float flow) {
		this.flow = flow;
	}

	public EventType getEvent() {
		return event;
	}

	public void setEvent(EventType event) {
		this.event = event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		long temp;
		temp = Double.doubleToLongBits(flow);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (!day.equals(other.day))
			return false;
		if (event != other.event)
			return false;
		if (Double.doubleToLongBits(flow) != Double.doubleToLongBits(other.flow))
			return false;
		return true;
	}

	@Override
	public int compareTo(Event arg0) {
		if (this.day.compareTo(arg0.day) == 0)
			return this.event.compareTo(arg0.event);
		return this.day.compareTo(arg0.day);
	}
}
