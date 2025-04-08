import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PrevisionServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_s2_ETU003183";
    private static final String DB_USER = "ETU003183";
    private static final String DB_PASSWORD = "9TXdAlaL"; 

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String categorie = request.getParameter("categorie");
        double montant = Double.parseDouble(request.getParameter("montant"));

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO Previsions (categorie, montant) VALUES (?, ?) ON DUPLICATE KEY UPDATE montant = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, categorie);
            stmt.setDouble(2, montant);
            stmt.setDouble(3, montant);
            stmt.executeUpdate();

            out.println("<html><body>");
            out.println("<h2>Prévision enregistrée</h2>");
            out.println("Catégorie: " + categorie + "<br>");
            out.println("Montant: " + montant + "<br>");
            out.println("<a href='depense'>Aller aux dépenses</a>");
            out.println("</body></html>");
        } catch (SQLException e) {
            out.println("Erreur: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h2>PREVISION</h2>");
        out.println("<form method='post'>");
        out.println("Page de previson: <input type='text' name='categorie'><br>");
        out.println("Montant: <input type='number' step='0.01' name='montant'><br>");
        out.println("<input type='submit' value='Appliquer'>");
        out.println("</form>");
        out.println("</body></html>");
    }
}