/* Populate tables */
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Fubuki', 'Shirakami', 'fbk@test.com', '2020-08-28', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Korone', 'Inugami', 'korone@test.com', '2020-08-28', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Okayu', 'Nekomata', 'okayu@test.com', '2020-08-29', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Mio', 'Ookami', 'mio@test.com', '2020-08-29', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Subaru', 'Oozora', 'sb@test.com', '2020-08-29', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Ayame', 'Nakiri', 'ayame@test.com', '2020-08-30', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Aqua', 'Minato', 'aqua@test.com', '2020-08-30', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Marine', 'Houshou', 'maririn@test.com', '2020-08-31', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Rushia', 'Uruha', 'rushia@test.com', '2020-08-31', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Pekora', 'Usada', 'peko@test.com', '2020-08-31', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Coco', 'Kiryu', 'kiryu@test.com', '2020-09-01', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Matsuri', 'Natsuiru', 'matsuri@test.com', '2020-09-01', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Botan', 'Shishiro', 'botan@test.com', '2020-09-01', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Polka', 'Omaru', 'polka@test.com', '2020-09-01', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Shion', 'Murasaki', 'shion@test.com', '2020-09-01', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Suisei', 'Hoshimachi', 'suisui@test.com', '2020-09-02', '');

/* Populate tabla productos */
INSERT INTO productos (nombre, precio, create_at) VALUES('Panasonic Pantalla LCD', 25990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Sony Camara Digital DSC-W320B', 12550, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Apple iPod Shuffle', 18690, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Sony Notebook Z110', 37450, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Hewlett Packard Multifunctional F2280', 40000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Bianchi Bicicleta Aro 24', 8990, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Mica Comoda 5 Cajones', 29990, NOW());

/* Creamos algunas facturas */
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES('Factura de equipos de oficina', null, 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(2, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 5);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 7);

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES('Factura Bicicleta', 'Alguna nota importante!', 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(3, 2, 6);

/* Creamos algunos usuarios con sus roles */
INSERT INTO `users` (username, password, enabled) VALUES('ivan', '$2a$10$O9wxmH/AeyZZzIS09Wp8YOEMvFnbRVJ8B4dmAMVSGloR62lj.yqXG', 1);
INSERT INTO `users` (username, password, enabled) VALUES('admin', '$2a$10$DOMDxjYyfZ/e7RcBfUpzqeaCs8pLgcizuiQWXPkU35nOhZlFcE9MS', 1);

INSERT INTO `authorities` (user_id, authority) VALUES(1, 'ROLE_USER');
INSERT INTO `authorities` (user_id, authority) VALUES(2, 'ROLE_USER');
INSERT INTO `authorities` (user_id, authority) VALUES(2, 'ROLE_ADMIN');