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

public class EtatServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_s2_ETU003183";
    private static final String DB_USER = "ETU003183";
    private static final String DB_PASSWORD = "9TXdAlaL"; // Remplacez par le mot de passe correct

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT p.categorie, p.montant AS prevision, COALESCE(SUM(d.montant), 0) AS depense " +
                         "FROM Previsions p LEFT JOIN Depenses d ON p.categorie = d.categorie " +
                         "GROUP BY p.categorie, p.montant";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            out.println("<html><body>");
            out.println("<h2>Page des finances</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>Categorie</th><th>Prevision</th><th>Depense</th><th>Reste</th></tr>");

            while (rs.next()) {
                String categorie = rs.getString("categorie");
                double prevision = rs.getDouble("prevision");
                double depense = rs.getDouble("depense");
                double reste = prevision - depense;

                out.println("<tr>");
                out.println("<td>" + categorie + "</td>");
                out.println("<td>" + prevision + "</td>");
                out.println("<td>" + depense + "</td>");
                out.println("<td>" + reste + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<br><a href='prevision'>Ajouter prevision</a>");
            out.println("</body></html>");
        } catch (SQLException e) {
            out.println("Erreur: " + e.getMessage());
        }
    }
}