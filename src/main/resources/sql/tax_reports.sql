CREATE TABLE IF NOT EXISTS `dbo_kundu`.`tax_reports` (
  `idtax_reports` INT NOT NULL AUTO_INCREMENT,
  `no_report` VARCHAR(105) NOT NULL,
  `desc_report` VARCHAR(245) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `user_id` INT NULL,
  PRIMARY KEY (`idtax_reports`),
  INDEX `fk_tax_reports_tsg_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_tax_reports_tsg_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `dbo_kundu`.`tsg_users` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;