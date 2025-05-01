/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistema_biblioteca;
import java.util.Scanner;
import java.sql.*;
import java.time.temporal.ChronoUnit;
/**
 *
 * @author User
 */
public class Sistema_Biblioteca {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        try {
            System.out.println("Sistema Biblioteca");
            mostrarMenu();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void mostrarMenu() throws SQLException {
        int opcion;
        do {
            System.out.println("\nMENU PRINCIPAL");
            System.out.println("1. Registrar nuevo libro");
            System.out.println("2. Registrar nuevo almacen");
            System.out.println("3. Registrar libro en almacen");
            System.out.println("4. Mostrar almacenes de libros");
            System.out.println("5. Registrar nuevo prestamo");
            System.out.println("6. Registrar devolucion");
            System.out.println("7. Consultar prestamos activos");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opcion: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch(opcion) {
                case 1:
                    registrarLibro();
                    break;
                case 2:
                    registrarBiblioteca();
                    break;
                case 3:
                    listarLibrosYBibliotecas();
                    break;
                case 4:
                    mostrarLibrosDeAlmacen();
                    break;
                case 5:
                    registrarPrestamo();
                    break;
                case 6:
                    registrarDevolucion();
                    break;
                case 7:
                    consultarPrestamosActivos();
                    break;
                case 8:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while(opcion != 8);
    }
    
    private static void registrarLibro() throws SQLException {
        System.out.println("\nREGISTRAR NUEVO LIBRO");
        
        int isbnLibro = 0;
        while (true) {
            System.out.print("ISBN del libro (6 digitos): ");
            try {
                String isbnCantidad = scanner.nextLine();
                if (isbnCantidad.length() != 6) {
                    System.out.println("Error: El ISBN debe tener exactamente 6 dígitos");
                    continue;
                }
                isbnLibro = Integer.parseInt(isbnCantidad);
                if (validarExistencia("libro", "isbn", isbnLibro)) {
                    System.out.println("Error: Codigo de libro ya existe");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar solo numeros para el ISBN");
            }
        }      
        
        System.out.print("Nombre del libro: ");
        String nombreLibro = scanner.nextLine();
        
        System.out.print("Nombre de la editorial: ");
        String nombreEditorial = scanner.nextLine();
        
        String sql = "INSERT INTO libro VALUES (?, ?, ?)";
        
        try (Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, isbnLibro);
            pstmt.setString(2, nombreLibro);
            pstmt.setString(3, nombreEditorial);
            
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow > 0){
                System.out.println("Se registro el nuevo libro");
                System.out.println("\nREGISTRE EL LIBRO EN EL ALMACEN DE UNA BIBLIOTECA");
                listarLibrosYBibliotecas();
            }else{
                System.out.println("No se pudo registrar el nuevo libro");
            }
        }
    }
    
    private static void registrarBiblioteca() throws SQLException {
        System.out.println("\nREGISTRAR NUEVA BIBLIOTECA");
        
        int idbiblioteca = 0;
        while (true) {
            System.out.print("ISBN de la biblioteca (4 digitos): ");
            try {
                String idCantidad = scanner.nextLine();
                if (idCantidad.length() != 4) {
                    System.out.println("Error: El ID debe tener exactamente 4 dígitos");
                    continue;
                }
                idbiblioteca = Integer.parseInt(idCantidad);
                if (validarExistencia("biblioteca_almacen", "id_biblioteca", idbiblioteca)) {
                    System.out.println("Error: ID de la biblioteca ya existe");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar solo numeros para el ID de la biblioteca");
            }
        }
        
        System.out.print("Nombre de la biblioteca: ");
        String nombreBiblioteca = scanner.nextLine();
        
        System.out.print("Direccion de la biblioteca: ");
        String direccionBiblioteca = scanner.nextLine();
        
        String sql = "INSERT INTO biblioteca_almacen VALUES (?, ?, ?)";
        
        try (Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, idbiblioteca);
            pstmt.setString(2, nombreBiblioteca);
            pstmt.setString(3, direccionBiblioteca);
            
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow > 0){
                System.out.println("Se registro la nueva biblioteca");
            }else{
                System.out.println("No se pudo registrar la nueva biblioteca");
            }
        }
    }
    
    private static void listarLibrosYBibliotecas() throws SQLException {
        System.out.println("\nREGISTRAR LIBRO EN ALMACEN");
        System.out.println("----------------------- LISTA DE LIBROS -----------------------");
        String sql = "SELECT isbn as isbn_libro, nombre as nombre_libro, editorial as editorial_libro FROM libro";
        try (Connection conn = Conexion.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            System.out.printf("%-8s %-30s %-12s%n", "ISBN", "NOMBRE DEL LIBRO", "EDITORIAL");
            
            while (rs.next()) {
                System.out.printf("%-8d %-30s %-12s%n", rs.getInt("isbn_libro"), rs.getString("nombre_libro"), rs.getString("editorial_libro"));
            }
        }
        System.out.println("---------------------- LISTA DE ALMACENES ----------------------");
        String sql_2 = "SELECT id_biblioteca as id_bib, nombre as nombre_bib, direccion as direccion_bib FROM biblioteca_almacen";
        try (Connection conn = Conexion.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql_2)){
            
            System.out.printf("%-16s %-30s %-20s%n", "ID BIBLIOTECA", "NOMBRE", "DIRECCION");            
            while (rs.next()) {
                System.out.printf("%-16d %-30s %-20s%n", rs.getInt("id_bib"), rs.getString("nombre_bib"), rs.getString("direccion_bib"));
            }
            System.out.println("----------------------------------------------------------------");
        }
        registrarLibroEnAlmacen();
    }
    
    private static void registrarLibroEnAlmacen() throws SQLException {
        System.out.println("\nREGISTRAR LIBRO EN ALMACEN");
        
        int isbnLibro = 0;
        while (true) {
            System.out.print("ISBN del libro (6 digitos): ");
            try {
                String isbnCantidad = scanner.nextLine();
                if (isbnCantidad.length() != 6) {
                    System.out.println("Error: El ISBN debe tener exactamente 6 digitos");
                    continue;
                }
                isbnLibro = Integer.parseInt(isbnCantidad);
                if (!validarExistencia("libro", "isbn", isbnLibro)) {
                    System.out.println("Error: Codigo de libro no encontrado");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar solo numeros para el ISBN");
            }
        }
        
        int idBiblioteca = 0;
        while (true) {
            System.out.print("ID de la biblioteca (4 digitos): ");
            try {
                String bibliotecaCantidad = scanner.nextLine();
                if (bibliotecaCantidad.length() != 4) {
                    System.out.println("Error: El ID de la biblioteca debe tener exactamente 4 digitos");
                    continue;
                }
                idBiblioteca = Integer.parseInt(bibliotecaCantidad);
                if (!validarExistencia("biblioteca_almacen", "id_biblioteca", idBiblioteca)) {
                    System.out.println("Error: ID de biblioteca no encontrado");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar solo numeros para el ID de la biblioteca");
            }
        }
        
        int stockLibro = 0;
        while (true) {
            System.out.print("Stock de libro en el almacen de la biblioteca: ");
            try {
                stockLibro = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar solo numeros para el Stock");
            }
        }
        
        if (validarLibroBiblioteca("almacen", "isbn_libro", "id_biblioteca", isbnLibro, idBiblioteca)){
            System.out.println("Ya existen libros en el almacen de esa biblioteca");
            return;
        }
                
        String sql = "INSERT INTO almacen VALUES (?, ?, ?)";
        
        try (Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, isbnLibro);
            pstmt.setInt(2, idBiblioteca);
            pstmt.setInt(3, stockLibro);
            
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow > 0){
                System.out.println("Se registro el libro en el almacen de la biblioteca");
            }else{
                System.out.println("No se pudo registro el libro en el almacen de la biblioteca");
            }
        }
    }
    
    private static void mostrarLibrosDeAlmacen() throws SQLException {
        System.out.println("\n------- LISTA DE LIBROS EN ALMACENES DE BIBLIOTECAS -------");
        String sql = "SELECT isbn_libro as isbn_lib, id_biblioteca as id_bib, stock as stock_lib FROM almacen";
        try (Connection conn = Conexion.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            System.out.printf("%-16s %-16s %-10s%n", "ISBN LIBRO", "ID BIBLIOTECA", "STOCK");
            
            while (rs.next()) {
                System.out.printf("%-16d %-16s %-10s%n", rs.getInt("isbn_lib"), rs.getInt("id_bib"), rs.getInt("stock_lib"));
            }
        }
    }
    
    private static void registrarPrestamo() throws SQLException {
        System.out.println("\nREGISTRAR NUEVO PRESTAMO");

        int dniCliente = 0;
        while (true) {
            System.out.print("DNI del cliente (8 digitos): ");
            try {
                dniCliente = Integer.parseInt(scanner.nextLine());
                if (String.valueOf(dniCliente).length() < 8) {
                    System.out.println("Error: El DNI debe tener al menos 8 digitos");
                    continue;
                }
                if (!validarExistencia("cliente", "dni_cliente", dniCliente)) {
                    System.out.println("Error: Cliente no encontrado");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar solo numeros para el DNI");
            }
        }

        int isbnLibro = 0;
        while (true) {
            System.out.print("ISBN del libro (6 digitos): ");
            try {
                String cantidadISBN = scanner.nextLine();
                if (cantidadISBN.length() != 6) {
                    System.out.println("Error: El ISBN debe tener exactamente 6 digitos");
                    continue;
                }
                isbnLibro = Integer.parseInt(cantidadISBN);
                if (!validarExistencia("libro", "isbn", isbnLibro)) {
                    System.out.println("Error: ISBN del libro no encontrado");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar solo numeros para el ISBN");
            }
        }

        int idBiblioteca = 0;
        while (true) {
            System.out.print("ID de la biblioteca (4 digitos): ");
            try {
                String bibliotecaCantidad = scanner.nextLine();
                if (bibliotecaCantidad.length() != 4) {
                    System.out.println("Error: El ID de la biblioteca debe tener exactamente 4 digitos");
                    continue;
                }
                idBiblioteca = Integer.parseInt(bibliotecaCantidad);
                if (!validarExistencia("biblioteca_almacen", "id_biblioteca", idBiblioteca)) {
                    System.out.println("Error: ID de biblioteca no encontrado");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar solo numeros para el ID de la biblioteca");
            }
        }

        if (!validarLibroEnBiblioteca(isbnLibro, idBiblioteca)) {
            System.out.println("Error: El libro no existe en la biblioteca especificada");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false);

            try {
                String checkStockSQL = "SELECT stock FROM almacen WHERE isbn_libro = ? AND id_biblioteca = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkStockSQL)) {
                    checkStmt.setInt(1, isbnLibro);
                    checkStmt.setInt(2, idBiblioteca);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        int stockActual = rs.getInt("stock");
                        if (stockActual <= 0) {
                            System.out.println("No hay ejemplares disponibles de este libro");
                            return;
                        }
                    } else {
                        System.out.println("El libro no existe en esta biblioteca");
                        return;
                    }
                }

                String updateStockSQL = "UPDATE almacen SET stock = stock - 1 WHERE isbn_libro = ? AND id_biblioteca = ? AND stock > 0";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateStockSQL)) {
                    updateStmt.setInt(1, isbnLibro);
                    updateStmt.setInt(2, idBiblioteca);
                    int updatedRows = updateStmt.executeUpdate();

                    if (updatedRows == 0) {
                        System.out.println("No hay Stock del libro");
                        conn.rollback();
                        return;
                    }
                }

                String insertPrestamoSQL = "INSERT INTO prestamo (dni_cliente, isbn_libro, id_biblioteca, "
                        + "fecha_prestamo, fecha_devolucion_esperada, estado) "
                        + "VALUES (?, ?, ?, ?, ?, 'activo')";

                Date fechaPrestamo = null;
                while (fechaPrestamo == null) {
                    System.out.print("Fecha de prestamo (YYYY-MM-DD): ");
                    try {
                        fechaPrestamo = Date.valueOf(scanner.nextLine());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Formato de fecha invalido. Use YYYY-MM-DD");
                    }
                }

                Date fechaDevolucionEsperada = null;
                while (fechaDevolucionEsperada == null){
                    System.out.print("Fecha esperada de devolucion (YYYY-MM-DD): ");
                    try {
                        fechaDevolucionEsperada = Date.valueOf(scanner.nextLine());
                    } catch (IllegalArgumentException e){
                        System.out.println("Formato de fecha invalido. Use YYYY-MM-DD");
                    }
                }
                
                try (PreparedStatement pstmt = conn.prepareStatement(insertPrestamoSQL)) {
                    pstmt.setInt(1, dniCliente);
                    pstmt.setInt(2, isbnLibro);
                    pstmt.setInt(3, idBiblioteca);
                    pstmt.setDate(4, fechaPrestamo);
                    pstmt.setDate(5, fechaDevolucionEsperada);

                    pstmt.executeUpdate();
                }

                System.out.println("Prestamo registrado exitosamente");

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Error al momento de realizar el prestamo: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    private static boolean validarLibroBiblioteca(String tabla, String columna1, String columna2, int valor1, int valor2) throws SQLException {
        String sql = "SELECT 1 FROM " + tabla + " WHERE " + columna1 + " = ? " + " AND " + columna2 + " = ?";
        try (Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, valor1);
            pstmt.setInt(2, valor2);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    private static boolean validarExistencia(String tabla, String columna, int valor) throws SQLException {
        String sql = "SELECT 1 FROM " + tabla + " WHERE " + columna + " = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, valor);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    private static boolean validarLibroEnBiblioteca(int isbnLibro, int idBiblioteca) throws SQLException {
        String sql = "SELECT 1 FROM almacen WHERE isbn_libro = ? AND id_biblioteca = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, isbnLibro);
            pstmt.setInt(2, idBiblioteca);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    private static void registrarDevolucion() throws SQLException {
        System.out.println("\nREGISTRAR DEVOLUCION");

        int idPrestamo = 0;
        while (true) {
            System.out.print("Ingrese el ID del prestamo: ");
            try {
                idPrestamo = Integer.parseInt(scanner.nextLine());
                if (!validarExistencia("prestamo", "id_prestamo", idPrestamo)) {
                    System.out.println("Error: Prestamo no encontrado");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar solo numeros para el ID del prestamo");
            }
        }

        Date fechaPrestamo = null;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT fecha_prestamo FROM prestamo WHERE id_prestamo = ? AND estado = 'activo'")) {

            stmt.setInt(1, idPrestamo);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Error: El prestamo ya fue devuelto");
                return;
            }
            fechaPrestamo = rs.getDate("fecha_prestamo");
        }

        Date fechaDevolucionReal = null;
        while (fechaDevolucionReal == null) {
            System.out.print("Fecha de devolucion (actual en YYYY-MM-DD): ");
            try {
                String input = scanner.nextLine();
                fechaDevolucionReal = Date.valueOf(input);

                if (fechaDevolucionReal.before(fechaPrestamo)) {
                    System.out.println("Error: La fecha de devolucion no puede ser anterior a la fecha de prestamo (" + fechaPrestamo + ")");
                    fechaDevolucionReal = null;
                    continue;
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Formato de fecha invalido (YYYY-MM-DD)");
            }
        }

        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false);

            try {
                String getPrestamoSQL = "SELECT isbn_libro, id_biblioteca, fecha_devolucion_esperada " +
                                        "FROM prestamo WHERE id_prestamo = ? AND estado = 'activo'";
                int isbnLibro, idBiblioteca;
                Date fechaEsperadaDevolucion;

                try (PreparedStatement getStmt = conn.prepareStatement(getPrestamoSQL)) {
                    getStmt.setInt(1, idPrestamo);
                    ResultSet rs = getStmt.executeQuery();

                    if (!rs.next()) {
                        System.out.println("El prestamo ya fue devuelto");
                        return;
                    }

                    isbnLibro = rs.getInt("isbn_libro");
                    idBiblioteca = rs.getInt("id_biblioteca");
                    fechaEsperadaDevolucion = rs.getDate("fecha_devolucion_esperada");
                }

                long diasRetraso = ChronoUnit.DAYS.between(
                    fechaEsperadaDevolucion.toLocalDate(),
                    fechaDevolucionReal.toLocalDate()
                );
                double multa = diasRetraso > 0 ? diasRetraso * 5.00 : 0.00;

                String updatePrestamoSQL = "UPDATE prestamo SET fecha_devolucion_real = ?, estado = 'devuelto', " +
                                          "multa = ? WHERE id_prestamo = ?";

                try (PreparedStatement updateStmt = conn.prepareStatement(updatePrestamoSQL)) {
                    updateStmt.setDate(1, fechaDevolucionReal);
                    updateStmt.setDouble(2, multa);
                    updateStmt.setInt(3, idPrestamo);
                    updateStmt.executeUpdate();
                }

                String updateStockSQL = "UPDATE almacen SET stock = stock + 1 WHERE isbn_libro = ? AND id_biblioteca = ?";
                try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSQL)) {
                    stockStmt.setInt(1, isbnLibro);
                    stockStmt.setInt(2, idBiblioteca);
                    stockStmt.executeUpdate();
                }

                conn.commit();
                System.out.println("Devolucion registrada exitosamente y stock actualizado");
                if (diasRetraso > 0) {
                    System.out.printf("Multa aplicada por %d días de retraso: $%.2f%n", diasRetraso, multa);
                }

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Error en la transacción: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    private static void consultarPrestamosActivos() throws SQLException {
        System.out.println("\nPRESTAMOS ACTIVOS");
        
        String sql = "SELECT p.id_prestamo, c.nombre AS nombre_cliente, c.apellido AS apellido_cliente, " +
                     "l.nombre AS nombre_libro, p.fecha_prestamo, p.fecha_devolucion_esperada " +
                     "FROM prestamo p " +
                     "JOIN cliente c ON p.dni_cliente = c.dni_cliente " +
                     "JOIN libro l ON p.isbn_libro = l.isbn " +
                     "WHERE p.estado = 'activo'";
        
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.printf("%-8s %-20s %-30s %-20s %-20s%n", "ID", "USUARIO", "LIBRO", "FECHA PRESTAMO", "FECHA DEVOLUCION");
            System.out.println("--------------------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-8d %-20s %-30s %-20s %-20s%n", rs.getInt("id_prestamo"), rs.getString("nombre_cliente") + " " + rs.getString("apellido_cliente"), rs.getString("nombre_libro"), rs.getDate("fecha_prestamo"), rs.getDate("fecha_devolucion_esperada"));
            }
        }
    }
}
