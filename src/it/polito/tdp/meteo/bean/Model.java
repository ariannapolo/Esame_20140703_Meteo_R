package it.polito.tdp.meteo.bean;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;

import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {
	
	private Pseudograph<Integer,DefaultEdge> graph;
	private Map<LocalDate,Integer> dataTemp;

	public List<LocalDate> getDate(int tMedia){
		MeteoDAO dao = new MeteoDAO();
		return dao.getDateConTemp(tMedia);
	}

	public void riempiMappa(){
		if(dataTemp==null){
			dataTemp = new HashMap<>();
			MeteoDAO dao = new MeteoDAO();
			dao.riempiMappa(dataTemp);
		}
	}
	public String getDateETMedie(int tMedia){
		MeteoDAO dao = new MeteoDAO();
		String res= "";
		for(LocalDate d : this.getDate(tMedia)){
			int t = dao.getTempGiornoPrecedente(d.minusDays(1));
			res+=d+" T-Media giorno precedente: "+t+"\n";
			//System.out.println(res);
		}
		return res;
	}
	
	public void creaGrafo(){
		graph = new Pseudograph<>(DefaultEdge.class);
		MeteoDAO dao = new MeteoDAO();
		//aggiungo vertici
		Graphs.addAllVertices(graph, dao.getTmedie());
		
		//aggiungo archi
		this.riempiMappa();
		for(LocalDate d : this.dataTemp.keySet()){
			LocalDate next = d.plusDays(1);
			if(dataTemp.get(next)!=null)
				graph.addEdge(dataTemp.get(d), dataTemp.get(next));
		}
		//System.out.println(graph);
	}
	
	public Set<Integer> analizza(int tMedia){
		Set<Integer> tempSucc = new HashSet<>();
		recursive(0,tMedia,tempSucc);
		return tempSucc;
	}
	
	
	
	private void recursive(int livello, int tMedia, Set<Integer> tempSucc) {
		if(livello>=2){
			return;
		}
		for(Integer t : Graphs.neighborListOf(graph, tMedia)){
			tempSucc.add(t);
			recursive(livello+1,t,tempSucc);
		}
		
	}

	public static void main(String args[]) {
		Model model = new Model();
		model.creaGrafo();
		System.out.println(model.analizza(25));
	}

}
