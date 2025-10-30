package org.example;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date; // Import necesario para los datos de ejemplo
import java.util.List;
import javax.persistence.NoResultException; // Import necesario para el Ej. 3

public class MainTrabajoPractico {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("example-unit");
        EntityManager em = emf.createEntityManager();

        System.out.println("--- Poblando la base de datos con datos de ejemplo... ---");
        try {
            // Persistir la entidad UnidadMedida en estado "gestionada"
            em.getTransaction().begin();
            // Crear una nueva entidad UnidadMedida en estado "nueva"
            UnidadMedida unidadMedida = UnidadMedida.builder()
                    .denominacion("Kilogramo")
                    .build();
            UnidadMedida unidadMedidapote = UnidadMedida.builder()
                    .denominacion("pote")
                    .build();

            em.persist(unidadMedida);
            em.persist(unidadMedidapote);


            // Crear una nueva entidad Categoria en estado "nueva"
            Categoria categoria = Categoria.builder()
                    .denominacion("Frutas")
                    .esInsumo(true)
                    .build();

            // Crear una nueva entidad Categoria en estado "nueva"
            Categoria categoriaPostre = Categoria.builder()
                    .denominacion("Postre")
                    .esInsumo(false)
                    .build();

            // Persistir la entidad Categoria en estado "gestionada"
            em.persist(categoria);
            em.persist(categoriaPostre);


            // Crear una nueva entidad ArticuloInsumo en estado "nueva"
            ArticuloInsumo articuloInsumo = ArticuloInsumo.builder()
                    .denominacion("Manzana").codigo(Long.toString(new Date().getTime()) + "M") // Se agrega sufijo para evitar colisión
                    .precioCompra(1.5)
                    .precioVenta(5d)
                    .stockActual(100)
                    .stockMaximo(200)
                    .esParaElaborar(true)
                    .unidadMedida(unidadMedida)
                    .build();


            ArticuloInsumo articuloInsumoPera = ArticuloInsumo.builder()
                    .denominacion("Pera").codigo(Long.toString(new Date().getTime()) + "P") // Se agrega sufijo para evitar colisión
                    .precioCompra(2.5)
                    .precioVenta(10d)
                    .stockActual(130)
                    .stockMaximo(200)
                    .esParaElaborar(true)
                    .unidadMedida(unidadMedida)
                    .build();

            // Persistir la entidad ArticuloInsumo en estado "gestionada"
            em.persist(articuloInsumo);
            em.persist(articuloInsumoPera);


            // Agregar el ArticuloInsumo a la Categoria
            categoria.getArticulos().add(articuloInsumo);
            categoria.getArticulos().add(articuloInsumoPera);

            // Crear una nueva entidad ArticuloManufacturadoDetalle en estado "nueva"
            ArticuloManufacturadoDetalle detalleManzana = ArticuloManufacturadoDetalle.builder()
                    .cantidad(2)
                    .articuloInsumo(articuloInsumo)
                    .build();

            ArticuloManufacturadoDetalle detallePera = ArticuloManufacturadoDetalle.builder()
                    .cantidad(2)
                    .articuloInsumo(articuloInsumoPera)
                    .build();

            // Crear una nueva entidad ArticuloManufacturado en estado "nueva"
            ArticuloManufacturado articuloManufacturado = ArticuloManufacturado.builder()
                    .denominacion("Ensalada de frutas")
                    .descripcion("Ensalada de manzanas y peras ")
                    .precioVenta(150d)
                    .tiempoEstimadoMinutos(10)
                    .preparacion("Cortar las frutas en trozos pequeños y mezclar")
                    .unidadMedida(unidadMedidapote)
                    .codigo(Long.toString(new Date().getTime()) + "E") // Código único
                    .build();

            categoriaPostre.getArticulos().add(articuloManufacturado);
            articuloManufacturado.getDetalles().add(detalleManzana);
            articuloManufacturado.getDetalles().add(detallePera);
            em.persist(articuloManufacturado);

            //creo y guardo un cliente
            Cliente cliente = Cliente.builder()
                    .cuit("20111111112") // REEMPLAZO DE FuncionApp
                    .razonSocial("Juan Perez")
                    .build();
            em.persist(cliente);

            //creo y guardo una factura
            FacturaDetalle detalle1 = new FacturaDetalle(3, articuloInsumo);
            detalle1.calcularSubTotal();
            FacturaDetalle detalle2 = new FacturaDetalle(3, articuloInsumoPera);
            detalle2.calcularSubTotal();
            FacturaDetalle detalle3 = new FacturaDetalle(3, articuloManufacturado);
            detalle3.calcularSubTotal();

            Factura factura = Factura.builder()
                    .puntoVenta(2024)
                    .fechaAlta(new Date())
                    .fechaComprobante(LocalDate.now().minusDays(15)) // REEMPLAZO DE FuncionApp (hace 15 días)
                    .cliente(cliente)
                    .nroComprobante(1L)
                    .build();
            factura.addDetalleFactura(detalle1);
            factura.addDetalleFactura(detalle2);
            factura.addDetalleFactura(detalle3);
            factura.calcularTotal();

            em.persist(factura);

            // Segunda factura para el mismo cliente (para Ej. 3)
            Factura factura2 = Factura.builder()
                    .puntoVenta(2024)
                    .fechaAlta(new Date())
                    .fechaComprobante(LocalDate.now().minusDays(5))
                    .cliente(cliente)
                    .nroComprobante(2L)
                    .build();
            FacturaDetalle detalle4 = new FacturaDetalle(10, articuloInsumo); // Otra de Manzana
            detalle4.calcularSubTotal();
            factura2.addDetalleFactura(detalle4);
            factura2.calcularTotal();
            em.persist(factura2);

            em.getTransaction().commit();
            System.out.println("--- ¡Base de datos poblada con éxito! ---");

        } catch (Exception ex) {
            System.err.println("--- Error al poblar la base de datos ---");
            em.getTransaction().rollback();
            ex.printStackTrace();
        }

        System.out.println("\n--- Iniciando Consultas del Trabajo Práctico JPQL ---");

        try {

            // --- Ejercicio 1: Listar todos los clientes ---
            System.out.println("\n--- Ejercicio 1: Listar todos los clientes ---");
            List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class)
                    .getResultList();
            for (Cliente c : clientes) {
                System.out.println("ID: " + c.getId() + ", Razón Social: " + c.getRazonSocial());
            }

            // --- Ejercicio 2: Listar todas las facturas generadas en el último mes ---
            System.out.println("\n--- Ejercicio 2: Listar todas las facturas generadas en el último mes ---");
            LocalDate fechaDesde = LocalDate.now().minusMonths(1);
            List<Factura> facturasMes = em.createQuery("SELECT f FROM Factura f WHERE f.fechaComprobante >= :fechaDesde", Factura.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .getResultList();
            for (Factura f : facturasMes) {
                System.out.println("ID: " + f.getId() + ", Fecha: " + f.getFechaComprobante() + ", Total: " + f.getTotal());
            }

            // --- Ejercicio 3: Obtener el cliente que ha generado más facturas ---
            System.out.println("\n--- Ejercicio 3: Obtener el cliente que ha generado más facturas ---");
            try {
                Cliente clienteMasFacturas = em.createQuery(
                                "SELECT c FROM Factura f JOIN f.cliente c GROUP BY c ORDER BY COUNT(f) DESC", Cliente.class)
                        .setMaxResults(1)
                        .getSingleResult();
                System.out.println("Cliente: " + clienteMasFacturas.getRazonSocial());
            } catch (NoResultException e) {
                System.out.println("No se encontraron facturas para determinar el cliente.");
            }

            // --- Ejercicio 4: Listar los artículos más vendidos ---
            System.out.println("\n--- Ejercicio 4: Listar los artículos más vendidos ---");
            List<Object[]> articulosMasVendidos = em.createQuery(
                            "SELECT d.articulo, SUM(d.cantidad) AS totalVendido FROM FacturaDetalle d GROUP BY d.articulo ORDER BY totalVendido DESC", Object[].class)
                    .getResultList();
            for (Object[] row : articulosMasVendidos) {
                Articulo art = (Articulo) row[0];
                Double total = (Double) row[1]; // SUM(d.cantidad) es Double
                System.out.println("Artículo: " + art.getDenominacion() + ", Cantidad Vendida: " + total);
            }

            // --- Ejercicio 5: Facturas de un cliente en los últimos 3 meses ---
            System.out.println("\n--- Ejercicio 5: Facturas de un cliente (ID=1) en los últimos 3 meses ---");
            Long clienteIdEj5 = 1L;
            LocalDate fechaDesde3Meses = LocalDate.now().minusMonths(3);
            List<Factura> facturasCliente3Meses = em.createQuery(
                            "SELECT f FROM Factura f WHERE f.cliente.id = :clienteId AND f.fechaComprobante >= :fechaDesde", Factura.class)
                    .setParameter("clienteId", clienteIdEj5)
                    .setParameter("fechaDesde", fechaDesde3Meses)
                    .getResultList();
            for (Factura f : facturasCliente3Meses) {
                System.out.println("ID: " + f.getId() + ", Fecha: " + f.getFechaComprobante() + ", Total: " + f.getTotal());
            }

            // --- Ejercicio 6: Calcular el monto total facturado por un cliente ---
            System.out.println("\n--- Ejercicio 6: Monto total facturado por un cliente (ID=1) ---");
            Long clienteIdEj6 = 1L;
            Double totalFacturadoCliente = em.createQuery(
                            "SELECT SUM(f.total) FROM Factura f WHERE f.cliente.id = :clienteId", Double.class)
                    .setParameter("clienteId", clienteIdEj6)
                    .getSingleResult();
            System.out.println("Monto total facturado al cliente " + clienteIdEj6 + ": $" + (totalFacturadoCliente != null ? totalFacturadoCliente : 0));

            // --- Ejercicio 7: Listar los Artículos vendidos en una factura ---
            System.out.println("\n--- Ejercicio 7: Artículos vendidos en una factura (ID=1) ---");
            Long facturaIdEj7 = 1L;
            List<Articulo> articulosFactura = em.createQuery(
                            "SELECT d.articulo FROM FacturaDetalle d WHERE d.factura.id = :facturaId", Articulo.class)
                    .setParameter("facturaId", facturaIdEj7)
                    .getResultList();
            for (Articulo art : articulosFactura) {
                System.out.println("Artículo: " + art.getDenominacion());
            }

            // --- Ejercicio 8: Obtener el Artículo más caro vendido en una factura ---
            System.out.println("\n--- Ejercicio 8: Artículo más caro en una factura (ID=1) ---");
            Long facturaIdEj8 = 1L;
            try {
                Articulo articuloMasCaro = em.createQuery(
                                "SELECT d.articulo FROM FacturaDetalle d WHERE d.factura.id = :facturaId ORDER BY d.articulo.precioVenta DESC", Articulo.class)
                        .setParameter("facturaId", facturaIdEj8)
                        .setMaxResults(1)
                        .getSingleResult();
                System.out.println("Artículo: " + articuloMasCaro.getDenominacion() + ", Precio: $" + articuloMasCaro.getPrecioVenta());
            } catch (NoResultException e) {
                System.out.println("No se encontraron artículos en la factura " + facturaIdEj8);
            }

            // --- Ejercicio 9: Contar la cantidad total de facturas generadas ---
            System.out.println("\n--- Ejercicio 9: Cantidad total de facturas ---");
            Long totalFacturas = em.createQuery("SELECT COUNT(f) FROM Factura f", Long.class)
                    .getSingleResult();
            System.out.println("Total de facturas en el sistema: " + totalFacturas);

            // --- Ejercicio 10: Listar facturas con total > 100 ---
            System.out.println("\n--- Ejercicio 10: Facturas con total mayor a 100 ---");
            Double montoMinimo = 100.0;
            List<Factura> facturasMonto = em.createQuery("SELECT f FROM Factura f WHERE f.total > :montoMinimo", Factura.class)
                    .setParameter("montoMinimo", montoMinimo)
                    .getResultList();
            for (Factura f : facturasMonto) {
                System.out.println("ID: " + f.getId() + ", Nro: " + f.getNroComprobante() + ", Total: $" + f.getTotal());
            }

            // --- Ejercicio 11: Facturas que contienen un Artículo (por nombre) ---
            System.out.println("\n--- Ejercicio 11: Facturas que contienen el artículo 'Manzana' ---");
            String nombreArticulo = "Manzana";
            List<Factura> facturasConArticulo = em.createQuery(
                            "SELECT DISTINCT d.factura FROM FacturaDetalle d WHERE d.articulo.denominacion = :nombreArticulo", Factura.class)
                    .setParameter("nombreArticulo", nombreArticulo)
                    .getResultList();
            for (Factura f : facturasConArticulo) {
                System.out.println("ID Factura: " + f.getId() + ", Nro: " + f.getNroComprobante());
            }

            // --- Ejercicio 12: Listar Artículos filtrando por código parcial ---
            System.out.println("\n--- Ejercicio 12: Artículos con código parcial 'M' ---");
            String codigoParcial = "%M%";
            List<Articulo> articulosCodigo = em.createQuery("SELECT a FROM Articulo a WHERE a.codigo LIKE :codigoParcial", Articulo.class)
                    .setParameter("codigoParcial", codigoParcial)
                    .getResultList();
            for (Articulo art : articulosCodigo) {
                System.out.println("ID: " + art.getId() + ", Denominación: " + art.getDenominacion() + ", Código: " + art.getCodigo());
            }

            // --- Ejercicio 13: Artículos cuyo precio > promedio ---
            System.out.println("\n--- Ejercicio 13: Artículos con precio mayor al promedio ---");
            List<Articulo> articulosEncimaPromedio = em.createQuery(
                            "SELECT a FROM Articulo a WHERE a.precioVenta > (SELECT AVG(a2.precioVenta) FROM Articulo a2)", Articulo.class)
                    .getResultList();
            for (Articulo art : articulosEncimaPromedio) {
                System.out.println("ID: " + art.getId() + ", Denominación: " + art.getDenominacion() + ", Precio: $" + art.getPrecioVenta());
            }

            // --- Ejercicio 14: Explique y ejemplifique la cláusula EXISTS ---
            System.out.println("\n--- Ejercicio 14: Explique y ejemplifique la cláusula EXISTS ---");
            System.out.println("Explicación: La cláusula EXISTS se usa en una subconsulta para verificar si la subconsulta devuelve una o más filas.");
            System.out.println("Devuelve TRUE si la subconsulta encuentra al menos un registro, y FALSE si no.");
            System.out.println("Es útil para encontrar entidades que 'tienen' (o 'no tienen' con NOT EXISTS) una relación específica.");
            System.out.println("\nEjemplo: Clientes que SÍ tienen al menos una factura registrada:");

            List<Cliente> clientesConFacturas = em.createQuery(
                            "SELECT c FROM Cliente c WHERE EXISTS (SELECT 1 FROM Factura f WHERE f.cliente = c)", Cliente.class)
                    .getResultList();
            for (Cliente c : clientesConFacturas) {
                System.out.println("Cliente: " + c.getRazonSocial());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("¡Error durante la ejecución de las consultas!");
        } finally {
            em.close();
            emf.close();
            System.out.println("\n--- Consultas del Trabajo Práctico Finalizadas ---");
        }
    }
}