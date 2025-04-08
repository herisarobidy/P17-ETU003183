import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DepenseServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_s2_ETU003183";
    private static final String DB_USER = "ETU003183";
    private static final String DB_PASSWORD = "9TXdAlaL"; 

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String categorie = request.getParameter("categorie");
        double montantDepense = Double.parseDouble(request.getParameter("montant"));

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Vérifier la prévision et les dépenses actuelles
            String sqlPrevision = "SELECT montant FROM Previsions WHERE categorie = ?";
            PreparedStatement stmtPrevision = conn.prepareStatement(sqlPrevision);
            stmtPrevision.setString(1, categorie);
            ResultSet rs = stmtPrevision.executeQuery();

            if (!rs.next()) {
                out.println("Erreur: Aucune prévision pour cette catégorie");
                return;
            }

            double prevision = rs.getDouble("montant");

            String sqlDepense = "SELECT SUM(montant) as total_depense FROM Depenses WHERE categorie = ?";
            PreparedStatement stmtDepense = conn.prepareStatement(sqlDepense);
            stmtDepense.setString(1, categorie);
            ResultSet rsDepense = stmtDepense.executeQuery();
            double depenseActuelle = rsDepense.next() ? rsDepense.getDouble("total_depense") : 0.0;

            double nouveauTotalDepense = depenseActuelle + montantDepense;

            if (nouveauTotalDepense > prevision) {
                out.println("Erreur: Le montant dépasse la prévision!");
            } else {
                // Enregistrer la dépense
                String sqlInsert = "INSERT INTO Depenses (categorie, montant) VALUES (?, ?)";
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                stmtInsert.setString(1, categorie);
                stmtInsert.setDouble(2, montantDepense);
                stmtInsert.executeUpdate();

                out.println("<html><body>");
                out.println("<h2>Dépense enregistrée</h2>");
                out.println("Catégorie: " + categorie + "<br>");
                out.println("Montant dépensé: " + montantDepense + "<br>");
                out.println("<a href='etat'>Voir l'état</a>");
                out.println("</body></html>");
            }
        } catch (SQLException e) {
            out.println("Erreur: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h2>DEPENSE</h2>");
        out.println("<form method='post'>");
        out.println("Page de depense: <input type='text' name='categorie'><br>");
        out.println("Montant: <input type='number' step='0.01' name='montant'><br>");
        out.println("<input type='submit' value='Appliquer'>");
        out.println("</form>");
        out.println("</body></html>");
    }
}