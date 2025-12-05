
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Computer Keyboards', 'Keyboards');
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Computer Monitors', 'Monitors');
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Display Screens', 'Screens');
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Computer Mice', 'Mice');
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Computer Accessories', 'Accessories');


-- Assuming you already have a sequence named 'product_seq'

--Insert products for the 'Keyboards' category
INSERT INTO public.product (id, available_quantity, description, name, price, image_url,category_id)
VALUES
    (nextval('product_seq'), 90000, 'Mechanical keyboard with RGB lighting', 'Mechanical Keyboard 1', 99.99,'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/563258e4-3504-4f3d-b342-9fcee798ef4c_MechanicalKeyboard.jpg' ,(SELECT id FROM category WHERE name = 'Keyboards')),
    (nextval('product_seq'), 90000, 'Wireless compact keyboard', 'Wireless Compact Keyboard 1', 79.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/686ed532-4ac9-46e2-a7e9-51fb362d0a43_WirelessCompactKeyboard.jpg', (SELECT id FROM category WHERE name = 'Keyboards')),
    (nextval('product_seq'), 90000, 'Backlit gaming keyboard with customizable keys', 'Gaming Keyboard 1', 129.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/59be889a-f539-45ed-83ff-fe21d4e47b84_GamingKeyboard.png',  (SELECT id FROM category WHERE name = 'Keyboards')),
    (nextval('product_seq'), 90000, 'Mechanical keyboard with wrist rest', 'Ergonomic Keyboard 1', 109.99,'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/cdbbdf55-51dc-4d4b-b637-982d2d89ce65_ErgonomicKeyboard.jpg' ,(SELECT id FROM category WHERE name = 'Keyboards')),
    (nextval('product_seq'), 90000, 'Wireless keyboard and mouse combo', 'Wireless Combo 1', 69.99,'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/60906b10-82d3-4bd9-9eae-9442635673fe_WirelessCombo.jpg', (SELECT id FROM category WHERE name = 'Keyboards'));

-- Insert products for the 'Monitors' category  51
INSERT INTO public.product (id, available_quantity, description, name, price, image_url, category_id)
VALUES
    (nextval('product_seq'), 90000, '27-inch IPS monitor with 4K resolution', '4K Monitor 1', 399.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/5052e830-8a8c-4b8e-bfc6-fc8c32cb754a_4KMonitor.webp',(SELECT id FROM category WHERE name = 'Monitors')),
    (nextval('product_seq'), 90000, 'Ultra-wide gaming monitor with HDR support', 'Ultra-wide Gaming Monitor 1',499.99,'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/535b1e61-aa26-46c8-a35f-414c1f3a570b_Ultra-wideGamingMonitor.jpg', (SELECT id FROM category WHERE name = 'Monitors')),
    (nextval('product_seq'), 90000, '24-inch LED monitor for office use', 'Office Monitor 1', 179.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/4a62297b-7e7a-46b8-9cde-9cc24c1309d8_OfficeMonitor.webp',(SELECT id FROM category WHERE name = 'Monitors')),
    (nextval('product_seq'), 90000, '32-inch curved monitor with AMD FreeSync', 'Curved Monitor 1', 329.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/6ba74b90-d17d-4d1b-a197-651c5ef097dc_CurvedMonitor.webp',(SELECT id FROM category WHERE name = 'Monitors')),
    (nextval('product_seq'), 90000, 'Portable USB-C monitor for laptops', 'Portable Monitor 1', 249.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/81e3f1d7-21a9-4ce1-a80b-21db85597b93_PortableMonitor.webp',(SELECT id FROM category WHERE name = 'Monitors'));

--Insert products for the 'Screens' category 101
INSERT INTO public.product (id, available_quantity, description, name, price,image_url, category_id)
VALUES
    (nextval('product_seq'), 90000, 'Curved OLED gaming screen with 240Hz refresh rate', 'Curved OLED Gaming Screen 1', 799.99,'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/748814fa-5d02-403c-8fa1-23ab613e2243_CurvedOLEDGamingScreen.jpg', (SELECT id FROM category WHERE name = 'Screens')),
    (nextval('product_seq'), 90000, 'Flat QLED monitor with 1440p resolution', 'QLED Monitor 1', 599.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/1bbc8686-906b-4bf5-b325-ab2e4bb3fc54_QLEDMonitor.jpg',(SELECT id FROM category WHERE name = 'Screens')),
    (nextval('product_seq'), 90000, '27-inch touch screen display for creative work', 'Touch Screen Display 1', 699.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/f6b0777b-690e-4f6b-b297-f4f03cef9d8d_TouchScreenDisplay.jpg',(SELECT id FROM category WHERE name = 'Screens')),
    (nextval('product_seq'), 90000, 'Ultra-slim 4K HDR display for multimedia', 'Ultra-slim 4K HDR Display 1', 449.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/106cd99a-bbe1-4ef9-9909-6ea5a2097008_Ultra-slim4KHDRDisplay.webp',(SELECT id FROM category WHERE name = 'Screens')),
    (nextval('product_seq'), 90000, 'Gaming projector with low input lag', 'Gaming Projector 1', 899.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/56529d1c-34aa-4f46-9c62-076ba522392b_GamingProjector.webp',(SELECT id FROM category WHERE name = 'Screens'));

-- Insert products for the 'Mice' category 151
INSERT INTO public.product (id, available_quantity, description, name, price, image_url, category_id)
VALUES
    (nextval('product_seq'), 90000, 'Wireless gaming mouse with customizable RGB lighting', 'RGB Gaming Mouse 1', 59.99,'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/00227dda-9cb0-45f8-b87c-be7739f73b9c_RGBGamingMouse.jpg' ,(SELECT id FROM category WHERE name = 'Mice')),
    (nextval('product_seq'), 90000, 'Ergonomic wired mouse for productivity', 'Ergonomic Wired Mouse 1', 29.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/6d071449-c9a9-4b27-9f75-d8d947e9565e_ErgonomicWiredMouse.jpg',(SELECT id FROM category WHERE name = 'Mice')),
    (nextval('product_seq'), 90000, 'Ambidextrous gaming mouse with high DPI', 'Ambidextrous Gaming Mouse 1', 69.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/fdf1b5fe-5e58-4426-8fbd-aa1081dcf967_AmbidextrousGamingMouse.webp',(SELECT id FROM category WHERE name = 'Mice')),
    (nextval('product_seq'), 90000, 'Travel-sized compact mouse for laptops', 'Travel Mouse 1', 19.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/62c91282-39ce-447b-8b18-963f73218beb_TravelMouse.jpg',(SELECT id FROM category WHERE name = 'Mice')),
    (nextval('product_seq'), 90000, 'Vertical ergonomic mouse for reduced strain', 'Vertical Ergonomic Mouse 1', 39.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/44aa32f9-d4c1-46d0-bbf7-6ea209dc927e_VerticalErgonomicMouse.jpg',(SELECT id FROM category WHERE name = 'Mice'));

-- Insert products for the 'Accessories' category 201
INSERT INTO public.product (id, available_quantity, description, name, price, image_url, category_id)
VALUES
    (nextval('product_seq'), 90000, 'Adjustable laptop stand with cooling fan', 'Adjustable Laptop Stand 1', 34.99,'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/70578c42-acdc-46fb-89d1-c993d510cf81_AdjustableLaptopStand.jpg', (SELECT id FROM category WHERE name = 'Accessories')),
    (nextval('product_seq'), 90000, 'Wireless charging pad for smartphones', 'Wireless Charging Pad 1', 24.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/82f552eb-5c4c-4934-9353-b568a48ac7b5_WirelessChargingPad.webp',(SELECT id FROM category WHERE name = 'Accessories')),
    (nextval('product_seq'), 90000, 'Gaming headset stand with RGB lighting', 'RGB Headset Stand 1', 49.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/883d56e7-712f-459b-9d55-79de79c85c74_RGBHeadsetStand.jpg',(SELECT id FROM category WHERE name = 'Accessories')),
    (nextval('product_seq'), 90000, 'Bluetooth mechanical keypad for tablets', 'Bluetooth Keypad 1', 39.99, 'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/a8fd533d-1f32-4b9f-b982-b9ff94b2359c_BluetoothKeypad.jpg',(SELECT id FROM category WHERE name = 'Accessories')),
    (nextval('product_seq'), 90000, 'External hard drive enclosure with USB-C', 'External Hard Drive Enclosure 1', 29.99,'https://my-product-images-47.s3.ap-south-1.amazonaws.com/uploads/14fe286e-53ba-451b-9304-404abbab3cb4_ExternalHardDriveEnclosure.jpg' ,(SELECT id FROM category WHERE name = 'Accessories'));
