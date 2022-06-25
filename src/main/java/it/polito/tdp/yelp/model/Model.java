package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;



public class Model {
	private Graph<Business, DefaultWeightedEdge> grafo;
	private YelpDao dao;
	private List<Business> allBusiness;
	private Map<String,Business> vertici;
	private List<Centro> archi;
	
	private List<Business> migliore;
	private double pesoOttimo;
	private double soglia;
	
	public Model() {
		this.dao= new YelpDao();
		this.vertici= new HashMap<>();
		this.archi= new ArrayList<>();
		this.allBusiness= new ArrayList<>(this.dao.getAllBusiness());
	}
	
	public String creaGrafo(String citta) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo vertici
		this.vertici=this.dao.getVertici(citta);
		Graphs.addAllVertices(this.grafo, this.vertici.values());
		//aggiungo archi
		this.archi= this.dao.getArchi(citta, vertici);
		for(Centro c1: archi)
			for(Centro c2: archi)
				if(!c1.equals(c2))
					Graphs.addEdge(this.grafo, c1.getBusiness(), c2.getBusiness(), LatLngTool.distance(c1.getLocazione(), c2.getLocazione(), LengthUnit.KILOMETER));
		String s="GRAFO CREATO!\n";
		s+="#VERTICI: "+this.grafo.vertexSet().size()+"\n";
		s+="#ARCHI: "+this.grafo.edgeSet().size()+"\n";
		return s;
		
	}
	
	public List<String> getCitta(){
		List<String> citta = new ArrayList<>();
		for(Business b: allBusiness)
			if(!citta.contains(b.getCity()))
				citta.add(b.getCity());
		Collections.sort(citta);
		return citta;
	}

	public List<Business> getVertici() {
		List<Business> l = new ArrayList<>(this.vertici.values());
		Collections.sort(l);
		return l;
	}
	
	public Adiacenza getVicinoDistante(Business b){
		Adiacenza ris=null;
		double pesoOttimo=0;
		for(Business bus: Graphs.neighborListOf(this.grafo, b)) {
			DefaultWeightedEdge e = this.grafo.getEdge(b, bus);
			double peso = this.grafo.getEdgeWeight(e);
			if(peso>pesoOttimo) {
				ris = new Adiacenza(bus, peso);
				pesoOttimo=peso;
			}
				 
		}
		return ris;
		
	}
	
	public List<Business> calcolaPercorso(Business partenza, Business arrivo, double soglia){
		this.soglia=soglia;
		this.migliore= new ArrayList<>();
		List<Business> parziale = new ArrayList<>();
		parziale.add(partenza);
		cerca(parziale,arrivo);
		return migliore;
	}

	private void cerca(List<Business> parziale, Business arrivo) {
		Business ultimo = parziale.get(parziale.size()-1);
		//condizione di terminazione
		if(ultimo.equals(arrivo)) {
			double pesoTot=calcolaPeso(parziale);
			if(parziale.size()>migliore.size()) {
				pesoOttimo=pesoTot;
				migliore = new ArrayList<>(parziale);
			}
			return;
		}
		//Costruisco la ricorsione
		for(Business b: Graphs.neighborListOf(this.grafo, ultimo)) {
			if((!b.equals(arrivo) && b.getStars()>soglia) || b.equals(arrivo))
				if(!parziale.contains(b)) {
					parziale.add(b);
					cerca(parziale,arrivo);
					parziale.remove(parziale.size()-1);
				}
		}	
	}

	private double calcolaPeso(List<Business> parziale) {
		double peso=0;
		for(int i=0; i<parziale.size()-1;i++) {
			Business da = parziale.get(i);
			Business a = parziale.get(i+1);
			DefaultWeightedEdge e = this.grafo.getEdge(da, a);
			peso+=this.grafo.getEdgeWeight(e);
		}
		return peso;
	}

	public double getPesoOttimo() {
		// TODO Auto-generated method stub
		return this.pesoOttimo;
	}
	
	
	
}
