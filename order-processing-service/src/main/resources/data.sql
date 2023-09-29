--drop table if exists order_view;
create or replace view order_view as
select
  o.*,
  p.client,
  c.name client_name,
  sum(ol.cum_quantity) cum_quantity,
  sum(ol.cum_quantity * ol.cum_price) value,
  sum(ol.cum_quantity) = o.quantity is_complete
from trade_order o
join order_leg ol on o.id = ol.trade_order
join portfolio p on p.id = o.portfolio
join client c on c.id = p.client
group by o.id, p.id, client_name;
insert into client (id, email, password, name, role) values (
    '4821dde4-fcdd-11ed-be56-0242ac120002',
    'username@mail.com',
    'password',
    'name',
    0
) ON CONFLICT DO NOTHING;
insert into portfolio (id, name, is_default, client) values (
    '80148814-fcdd-11ed-be56-0242ac120002',
    'Example Portfolio',
    true,
    '4821dde4-fcdd-11ed-be56-0242ac120002'
) ON CONFLICT DO NOTHING;