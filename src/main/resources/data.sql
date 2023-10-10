INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email)VALUES ('123 Maple 
St','London','On', 'N1N-1N1','(555)555-5555','Trusted','ABC Supply Co.','abc@supply.com');
INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email) VALUES ('543 
Sycamore Ave','Toronto','On', 'N1P-1N1','(999)555-5555','Trusted','Big Bills 
Depot','bb@depot.com');
INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email) VALUES ('922 Oak 
St','London','On', 'N1N-1N1','(555)555-5599','Untrusted','Shady Sams','ss@underthetable.com');
INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email) VALUES ('777 London St',
'London','On', 'A1A 1A1','(555)555-5550','Untrusted','Agnita Paul','ap@vendorone.com');

INSERT INTO Product (Id, VendorId,Name,CostPrice,MSRP,ROP,EOQ, QOH, QOO, QRcode,QRcodeTxt)
 VALUES ('4O8UJEF',1,'Apple iPhone 13',1200.90, 1500.88,6,12,15, 2, '' ,'' );
INSERT INTO Product (Id, VendorId,Name,CostPrice,MSRP,ROP,EOQ, QOH, QOO, QRcode,QRcodeTxt)
 VALUES ('34URIE',2,'Microsoft Surface Pro 7',120.34, 150.65,7,19,14, 1, '','' );
INSERT INTO Product (Id, VendorId,Name,CostPrice,MSRP,ROP,EOQ, QOH, QOO, QRcode,QRcodeTxt)
 VALUES ('438UFJF',4,'Samsung Galaxy S21',1299.00, 1600.27,8,15,16, 0,'' , '');
INSERT INTO Product (Id, VendorId,Name,CostPrice,MSRP,ROP,EOQ, QOH, QOO, QRcode,QRcodeTxt)
 VALUES ('93UWHD',3,'Dell XPS 13',320.11, 500.66,5,13,15, 2,'' ,'' );
INSERT INTO Product (Id, VendorId,Name,CostPrice,MSRP,ROP,EOQ, QOH, QOO, QRcode,QRcodeTxt)
 VALUES ('DH4547FH',1,'Toyota Camry',400.30, 600.10,9,10,12, 4,'' , '');
INSERT INTO Product (Id, VendorId,Name,CostPrice,MSRP,ROP,EOQ, QOH, QOO, QRcode,QRcodeTxt)
 VALUES ('403RUFG',2,'Sony PlayStation 5',200.30, 300.40,7,11,11, 3, '','' );


