CREATE TABLE `ocean-demo4web`.`customer` (
  `id` BIGINT(20) NOT NULL  AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `contact` VARCHAR(255) NULL,
  `telephone` VARCHAR(255) NULL,
  `email` VARCHAR(255) NULL,
  `remark` TEXT CHARACTER SET 'utf8' NULL,
  PRIMARY KEY (`id`));

INSERT INTO `ocean-demo4web`.`customer` (`id`, `name`, `contact`, `telephone`, `email`) VALUES ('1', 'customer1', 'richey', '123123123123', '123@sdf.com');
INSERT INTO `ocean-demo4web`.`customer` (`id`, `name`, `contact`, `telephone`, `email`) VALUES ('2', 'customer2', 'jack', '31313131231', '312@sdfd.com');
