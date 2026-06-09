CREATE DATABASE drink_system2;
USE drink_system2;

CREATE TABLE user (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL
);
INSERT INTO user VALUES ('admin','123456');

CREATE TABLE drink (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT NOT NULL,
    category VARCHAR(30) NOT NULL,
    supplier VARCHAR(50),
    brand VARCHAR(30),
    spec VARCHAR(30),
    status INT DEFAULT 1
);

INSERT INTO drink VALUES
('D001','可口可乐',3.5,100,'碳酸饮料','可口可乐公司','可口可乐','500ml',1),
('D002','百事可乐',3.5,90,'碳酸饮料','百事公司','百事','500ml',1),
('D003','雪碧',3.5,80,'碳酸饮料','可口可乐公司','雪碧','500ml',1),
('D004','冰红茶',4,60,'茶饮','统一','统一冰红茶','500ml',1),
('D005','绿茶',4,70,'茶饮','统一','统一绿茶','500ml',1),
('D006','农夫山泉',2,200,'矿泉水','农夫山泉','农夫山泉','550ml',1),
('D007','怡宝',2,190,'矿泉水','怡宝','怡宝','550ml',1),
('D008','红牛',6,50,'功能饮料','红牛公司','红牛','250ml',1),
('D009','东鹏特饮',5,60,'功能饮料','东鹏','东鹏特饮','250ml',1),
('D010','珍珠奶茶',12,30,'奶茶','蜜雪冰城','蜜雪冰城','中杯',1),
('D011','椰果奶茶',12,25,'奶茶','蜜雪冰城','蜜雪冰城','中杯',1),
('D012','橙汁',5,40,'果汁','汇源','汇源','300ml',1),
('D013','苹果汁',5,35,'果汁','汇源','汇源','300ml',1);

CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    drink_id VARCHAR(20) NOT NULL,
    drink_name VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    total DOUBLE NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);