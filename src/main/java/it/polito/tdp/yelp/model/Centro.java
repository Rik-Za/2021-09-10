package it.polito.tdp.yelp.model;

import com.javadocmd.simplelatlng.LatLng;

public class Centro {
	private Business business;
	private LatLng locazione;
	public Centro(Business business, LatLng locazione) {
		super();
		this.business = business;
		this.locazione = locazione;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public LatLng getLocazione() {
		return locazione;
	}
	public void setLocazione(LatLng locazione) {
		this.locazione = locazione;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((business == null) ? 0 : business.hashCode());
		result = prime * result + ((locazione == null) ? 0 : locazione.hashCode());
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
		Centro other = (Centro) obj;
		if (business == null) {
			if (other.business != null)
				return false;
		} else if (!business.equals(other.business))
			return false;
		if (locazione == null) {
			if (other.locazione != null)
				return false;
		} else if (!locazione.equals(other.locazione))
			return false;
		return true;
	}
	
	

}
