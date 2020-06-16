package it.polito.tdp.crimes.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.db.EventsDao;
import it.polito.tdp.crimes.model.Evento.TipoEvento;

public class Simulatore {
	//variabili del mondo
	private Integer N;
	private Integer anno;
	private Integer mese;
	private Integer giorno;
	
	//stato del sistema
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private Map<Integer, Integer> agenti;//mappa distretto-->#agenti liberi
	//coda degli eventi
	private PriorityQueue<Evento> coda;
	
	//output
	private Integer malGestiti=0;
	
	public void init(Integer N, Integer anno, Integer mese, Integer giorno,Graph<Integer, DefaultWeightedEdge> grafo) {
		this.N=N;
		this.anno=anno;
		this.mese=mese;
		this.giorno=giorno;
		this.grafo=grafo;
		this.malGestiti=0;
		this.agenti=new HashMap<Integer,Integer>();
		for(Integer d:this.grafo.vertexSet()) {
			this.agenti.put(d,0);
		}
		//devo scegliere dove è la centrale e inserire N agenti in quel distretto
		EventsDao dao=new EventsDao();
		Integer minD=dao.getDistrettoMin(anno);
		this.agenti.put(minD, N);
		
		//creo e inizializzo la coda
		this.coda=new PriorityQueue<Evento>();
		
		for(Event e:dao.listAllEventsByDate(anno, mese, giorno)) {
			this.coda.add(new Evento(TipoEvento.CRIMINE, e.getReported_date(),e));
		}
		
		
	}
	
	public int run() {
		Evento e;
		while((e=this.coda.poll())!=null) {
			processEvent(e);
		}
		return this.malGestiti;
	}

	private void processEvent(Evento e) {
		switch(e.getTipo()) {
		case ARRIVA_AGENTE:
			System.out.println("ARRIVA AGENTE PER CRIMINE "+e.getCrimine().getIncident_id());
			Long duration=getDurata(e.getCrimine().getOffense_category_id());
			this.coda.add(new Evento(TipoEvento.GESTITO,e.getData().plusSeconds(duration),e.getCrimine()));
			//controllare se il crimine è mal gestito, ovvero se l'agente ha più di 15 min di ritardo
			if(e.getData().isAfter(e.getCrimine().getReported_date().plusMinutes(15))){
				//crimine mal gestito
				this.malGestiti++;
			}
			break;
		case CRIMINE:
			System.out.println("NUOVO CRIMINE: "+e.getCrimine().getIncident_id());
			//cerco l'agente libero più vicino
			Integer partenza=null;//distretto dal quale l'eventuale agente libero, parte
			partenza=cercaAgente(e.getCrimine().getDistrict_id());
			if(partenza!=null) {
				//c'è un agente libero in partenza
				//setto l'agente come occupato
				this.agenti.put(partenza, this.agenti.get(partenza)-1);
				//calcolo la distanza tra i distretti per capire quanto ci mette l'agente libero a muoversi
				Double distanza;
				if(partenza==e.getCrimine().getDistrict_id()) {
					distanza=0.0;
				}else {
					distanza=this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, e.getCrimine().getDistrict_id()));
				}
				Long seconds=(long)((distanza*1000)/(60/3.6));//tempo di spostamento dell'agente tra i due distretti
				this.coda.add(new Evento(TipoEvento.ARRIVA_AGENTE,e.getData().plusSeconds(seconds),e.getCrimine()));
			}else {
				//non c'è nessun agente libero, crimine mal gestito
				System.out.println("CRIMINE "+e.getCrimine().getIncident_id()+" MAL GESTITO.");
				this.malGestiti++;
			}
			
			break;
		case GESTITO:
			System.out.println("CRIMINE "+e.getCrimine().getIncident_id()+" GESTITO.");
			this.agenti.put(e.getCrimine().getDistrict_id(),this.agenti.get(e.getCrimine().getDistrict_id())+1);
			break;
		default:
			break;
		
		}
	}

	private Long getDurata(String offense_category_id) {
		if(offense_category_id.equals("all_other_crimes")) {
			Random r=new Random();
			if(r.nextDouble()>0.5) {
				return Long.valueOf(2*60*60);
			}else
				return Long.valueOf(1*60*60);
		}else 
			return Long.valueOf(2*60*60);
	}

	private Integer cercaAgente(Integer district_id) {
		Double distanza=Double.MAX_VALUE;
		Integer distretto=null;
		for(Integer d:this.agenti.keySet()) {
			if(this.agenti.get(d)>0) {
				if(district_id.equals(d)) {
					distanza=Double.valueOf(0);
					distretto=d;
				}else if(this.grafo.getEdgeWeight(this.grafo.getEdge(district_id, d))<distanza){
					distanza=this.grafo.getEdgeWeight(this.grafo.getEdge(district_id, d));
				}
			}
			
		}
		
		return null;
	}
}















