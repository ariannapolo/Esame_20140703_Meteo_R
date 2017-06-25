/***************************************************************************\
 *               *                                                         *
 *    #####      *  (!) 2014 by Giovanni Squillero                         *
 *   ######      *  Politecnico di Torino - Dip. Automatica e Informatica  *
 *   ###   \     *  Cso Duca degli Abruzzi 24 / I-10129 TORINO / ITALY     *
 *    ##G  c\    *                                                         *
 *    #     _\   *  tel : +39-011-564.7092  /  Fax: +39-011-564.7099       *
 *    |   _/     *  mail: giovanni.squillero@polito.it                     *
 *    |  _/      *  www : http://www.cad.polito.it/staff/squillero/        *
 *               *                                                         *
\***************************************************************************/

package it.polito.tdp.meteo.db;

import it.polito.tdp.meteo.bean.Situazione;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe DAO per l'accesso al database {@code meteo}
 * 
 * @author Fulvio
 * 
 */
public class MeteoDAO {

	/**
	 * Interroga il database e restituisce tutti i dati nella tabella
	 * {@code situazione} sotto forma di un {@link ArrayList} di
	 * {@link Situazione}, ordinati in modo crescente per data.
	 * 
	 * @return la {@link ArrayList} di {@link Situazione}
	 */
	public List<Situazione> getAllSituazioni() {

		final String sql = "SELECT * FROM situazione ORDER BY data ASC";

		List<Situazione> situazioni = new ArrayList<Situazione>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Situazione s = new Situazione(rs.getString("Localita"),
						rs.getDate("Data"), rs.getInt("Tmedia"),
						rs.getInt("Tmin"), rs.getInt("Tmax"),
						rs.getInt("Puntorugiada"), rs.getInt("Umidita"),
						rs.getInt("Visibilita"), rs.getInt("Ventomedia"),
						rs.getInt("Ventomax"), rs.getInt("Raffica"),
						rs.getInt("Pressioneslm"), rs.getInt("Pressionemedia"),
						rs.getInt("Pioggia"), rs.getString("Fenomeni"));
				situazioni.add(s);
			}
			conn.close();
			return situazioni;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<LocalDate> getDateConTemp(int tMedia){
		final String sql = "SELECT `Data` FROM situazione WHERE Localita=\"Torino\" AND Tmedia = ?";

		List<LocalDate> date = new ArrayList<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, tMedia);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				date.add(rs.getDate("Data").toLocalDate());
			}
			conn.close();
			return date;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	public int getTempGiornoPrecedente(LocalDate d) {
		final String sql = "SELECT Tmedia FROM situazione WHERE `Data`= ? AND Localita = \"Torino\"";

		int tMedia = 0;
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDate(1,Date.valueOf(d));

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				tMedia = rs.getInt("Tmedia");
			}
			conn.close();
			return tMedia;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	public List<Integer> getTmedie(){
		final String sql = "SELECT DISTINCT(Tmedia) as t FROM situazione WHERE Localita = \"Torino\"";

		List<Integer> temp = new ArrayList<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				temp.add(rs.getInt("t"));
			}
			conn.close();
			return temp;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	public void riempiMappa(Map<LocalDate, Integer> dataTemp) {
		final String sql = "SELECT `Data`,Tmedia FROM situazione WHERE Localita = \"Torino\"";



		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				dataTemp.put(rs.getDate("Data").toLocalDate(), rs.getInt("Tmedia"));
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
		
	/**
	 * Test method for class {@link MeteoDAO}
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		
		MeteoDAO dao = new MeteoDAO() ;
		
		List<Situazione> list = dao.getAllSituazioni() ;
		
		for( Situazione s : list ) {
			System.out.format("%-10s %2td/%2$2tm/%2$4tY %3d°C-%3d°C  %3d%%  %s\n", 
					s.getLocalita(), s.getData(), s.getTMin(), s.getTMax(), s.getUmidita(), s.getFenomeni()) ;
		}

	}



	

}
