# Enunciado original — añadir artículos con JDBC y JComboBox

Fuente: `Ejercicio Insertar.pdf` del bloque **Acceso a BD con JDBC** conservado en Google Drive.

## Ejercicio

Entra en phpMyAdmin y crea una base de datos llamada **`tienda`**. A continuación, abre la base de datos e importa el archivo **`tienda.sql`**. Comprueba que se han creado correctamente las tablas de artículos, marcas y familias.

Crea un programa que permita al usuario **añadir un artículo**. Solo se podrán añadir artículos de las marcas y familias disponibles. Por lo tanto, son necesarios dos `ComboBox` donde aparecerán las listas de las marcas y familias disponibles.

Una vez seleccionadas la marca y la familia se rellenarán todos los datos del artículo nuevo. Al pulsar el botón **Añadir** se añadirá el artículo a la base de datos.

### Sentencias SQL indicadas en el enunciado

Para obtener las diferentes marcas y familias:

```sql
SELECT nombre FROM marcas;
SELECT nombre FROM familias;
```

Para obtener los códigos de marca y familia una vez seleccionados:

```sql
SELECT codigo FROM marcas WHERE nombre = ?;
SELECT codigo FROM familias WHERE nombre = ?;
```

Para añadir un artículo:

```sql
INSERT INTO articulos
(cod_marca, modelo, precio, descuento, descripcion, cod_familia)
VALUES (?, ?, ?, ?, ?, ?);
```

La base de datos original requerida por el ejercicio se conserva en [`database/tienda.sql`](database/tienda.sql).
