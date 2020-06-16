package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<Integer,DefaultWeightedEdge> grafo;
	
	public Model() {
		this.dao=new EventsDao();
	}
	
	public List<Integer> getAnni(){
		return dao.getAnni();
	}
	
	public void creaGrafo(Integer anno) {
		List<Integer> vertici=dao.getVertici();
		this.grafo=new SimpleWeightedGraph<Integer,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, vertici);
		for(Integer v1:this.grafo.vertexSet()) {
			for(Integer v2:this.grafo.vertexSet()) {
				if(!v1.equals(v2)) {
					if(this.grafo.getEdge(v1, v2)==null) {
						Double latMediaV1=dao.getLatMedia(anno,v1);
						Double latMediaV2=dao.getLatMedia(anno,v2);
						Double longMediaV1=dao.getLongMedia(anno,v1);
						Double longMediaV2=dao.getLongMedia(anno,v2);
						
						Double distanzaMedia=LatLngTool.distance(new LatLng(latMediaV1,longMediaV1),new LatLng(latMediaV2,longMediaV2),LengthUnit.KILOMETER);
						
						Graphs.addEdgeWithVertices(this.grafo, v1, v2, distanzaMedia);
					}
				}
			}
		}
		System.out.println("Grafo creato!\n");
		System.out.println("#VERTICI: "+this.grafo.vertexSet().size());
		System.out.println("#ARCHI: "+this.grafo.edgeSet().size());
	}
	
	public List<Vicino> getVicini(Integer distretto){
		List<Vicino> vicini=new ArrayList<>();
		
		List<Integer> viciniID=Graphs.neighborListOf(this.grafo, distretto);
		
		for(Integer v:viciniID) {
			vicini.add(new Vicino(v,this.grafo.getEdgeWeight(this.grafo.getEdge(distretto, v))));
		}
		Collections.sort(vicini);
		return vicini;
	}

	public Set<Integer> getVertici() {
		return this.grafo.vertexSet();
	}
	public Integer simula(Integer anno, Integer mese, Integer giorno, Integer N) {
		Simulatore sim= new Simulatore();
		sim.init(N, anno, mese, giorno, grafo);
		return sim.run();
	}

	public List<Integer> getGiorni() {
		return dao.getGiorni();
	}

	public List<Integer> getMesi() {
		return dao.getMesi();
	}
	
}
