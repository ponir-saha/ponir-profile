alter table site_profile add column whatsapp_url varchar(500);

update site_profile
set whatsapp_url = 'https://wa.me/8801713177318'
where id = 1 and (whatsapp_url is null or whatsapp_url = '');
