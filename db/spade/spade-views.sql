-- phpMyAdmin SQL Dump
-- version 5.0.4deb2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 10, 2022 at 05:24 PM
-- Server version: 10.5.15-MariaDB-0+deb11u1
-- PHP Version: 7.4.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `spade`
--
USE `spade`;

-- --------------------------------------------------------

--
-- Stand-in structure for view `artifactView`
-- (See below for the actual view)
--
CREATE TABLE `artifactView` (
`name` varchar(255)
,`description` longtext
,`created` datetime
,`url` varchar(255)
,`id` bigint(20)
,`artifactClass` varchar(255)
,`mimeType` varchar(255)
,`size` bigint(20)
,`authorId` bigint(20)
,`authorName` varchar(255)
,`projectId` bigint(20)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `commitedConfigView`
-- (See below for the actual view)
--
CREATE TABLE `commitedConfigView` (
`id` bigint(20)
,`type` varchar(31)
,`name` varchar(255)
,`description` longtext
,`created` datetime
,`authorId` bigint(20)
,`authorName` varchar(255)
,`relationName` varchar(255)
,`relatedId` bigint(20)
,`relatedName` varchar(255)
,`projectId` bigint(20)
,`committed` datetime
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `commitView`
-- (See below for the actual view)
--
CREATE TABLE `commitView` (
`id` bigint(20)
,`type` varchar(31)
,`name` varchar(255)
,`description` longtext
,`created` datetime
,`authorId` bigint(20)
,`authorName` varchar(255)
,`relationName` varchar(255)
,`relatedId` bigint(20)
,`relatedName` varchar(255)
,`projectId` bigint(20)
,`committed` datetime
,`isRelease` bit(1)
,`tag` varchar(255)
,`branch` varchar(255)
,`main` bit(1)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `configurationView`
-- (See below for the actual view)
--
CREATE TABLE `configurationView` (
`id` bigint(20)
,`type` varchar(31)
,`name` varchar(255)
,`description` longtext
,`created` datetime
,`authorId` bigint(20)
,`authorName` varchar(255)
,`relationName` varchar(255)
,`relatedId` bigint(20)
,`relatedName` varchar(255)
,`projectId` bigint(20)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `fieldChangeView`
-- (See below for the actual view)
--
CREATE TABLE `fieldChangeView` (
`id` bigint(20)
,`type` varchar(31)
,`name` varchar(255)
,`description` longtext
,`created` datetime
,`authorId` bigint(20)
,`authorName` varchar(255)
,`relationName` varchar(255)
,`relatedId` bigint(20)
,`relatedName` varchar(255)
,`projectId` bigint(20)
,`changeName` varchar(255)
,`changeDesc` longtext
,`itemId` bigint(20)
,`itemType` varchar(31)
,`itemName` varchar(255)
,`itemDesc` longtext
,`itemCreated` datetime
,`field` varchar(255)
,`newValue` longtext
,`oldValue` longtext
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `personView`
-- (See below for the actual view)
--
CREATE TABLE `personView` (
`id` bigint(20)
,`name` varchar(255)
,`projectId` bigint(20)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `personWithRolesView`
-- (See below for the actual view)
--
CREATE TABLE `personWithRolesView` (
`id` bigint(20)
,`name` varchar(255)
,`role` varchar(255)
,`roleClass` varchar(255)
,`roleSuperClass` varchar(255)
,`projectId` bigint(20)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `workUnitView`
-- (See below for the actual view)
--
CREATE TABLE `workUnitView` (
`id` bigint(20)
,`name` varchar(255)
,`description` longtext
,`created` datetime
,`dueDate` date
,`estimatedTime` double
,`progress` int(11)
,`spentTime` double
,`startDate` date
,`projectId` bigint(20)
,`authorId` bigint(20)
,`authorName` varchar(255)
,`assigneeId` bigint(20)
,`assigneeName` varchar(255)
,`activityName` varchar(255)
,`activityDesc` longtext
,`activityEndDate` date
,`activityStartDate` date
,`iterationName` varchar(255)
,`iterationDesc` longtext
,`iterationStartDate` date
,`iterationEndDate` date
,`iterationCreated` datetime
,`phaseName` varchar(255)
,`phaseDesc` longtext
,`phaseStartDate` date
,`phaseEndDate` date
,`phaseCreated` datetime
,`priorityName` varchar(255)
,`priorityDesc` longtext
,`prioClass` varchar(255)
,`prioSuperClass` varchar(255)
,`severityName` varchar(255)
,`severityDesc` longtext
,`severityClass` varchar(255)
,`severitySuperClass` varchar(255)
,`resolutionName` varchar(255)
,`resolutionDescription` longtext
,`resolutionClass` varchar(255)
,`resolutionSuperClass` varchar(255)
,`statusName` varchar(255)
,`statusDescription` longtext
,`statusClass` varchar(255)
,`statusSuperClass` varchar(255)
,`wuTypeName` varchar(255)
,`wuTypeDescription` longtext
,`wuTypeClass` varchar(255)
,`categoryName` varchar(255)
,`categoryDesc` longtext
);

-- --------------------------------------------------------

--
-- Structure for view `artifactView`
--
DROP TABLE IF EXISTS `artifactView`;

CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `artifactView`  AS SELECT `wi`.`name` AS `name`, `wi`.`description` AS `description`, `wi`.`created` AS `created`, `wi`.`url` AS `url`, `a`.`id` AS `id`, `a`.`artifactClass` AS `artifactClass`, `a`.`mimeType` AS `mimeType`, `a`.`size` AS `size`, `p`.`id` AS `authorId`, `p`.`name` AS `authorName`, `p`.`projectId` AS `projectId` FROM ((`work_item` `wi` join `artifact` `a` on(`a`.`id` = `wi`.`id`)) join `personView` `p` on(`p`.`id` = `wi`.`authorId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `commitedConfigView`
--
DROP TABLE IF EXISTS `commitedConfigView`;

CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `commitedConfigView`  AS SELECT `c`.`id` AS `id`, `c`.`type` AS `type`, `c`.`name` AS `name`, `c`.`description` AS `description`, `c`.`created` AS `created`, `c`.`authorId` AS `authorId`, `c`.`authorName` AS `authorName`, `c`.`relationName` AS `relationName`, `c`.`relatedId` AS `relatedId`, `c`.`relatedName` AS `relatedName`, `c`.`projectId` AS `projectId`, `cc`.`committed` AS `committed` FROM (`configurationView` `c` join `committed_configuration` `cc` on(`c`.`id` = `cc`.`id`)) ;

-- --------------------------------------------------------

--
-- Structure for view `commitView`
--
DROP TABLE IF EXISTS `commitView`;

CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `commitView`  AS SELECT `cc`.`id` AS `id`, `cc`.`type` AS `type`, `cc`.`name` AS `name`, `cc`.`description` AS `description`, `cc`.`created` AS `created`, `cc`.`authorId` AS `authorId`, `cc`.`authorName` AS `authorName`, `cc`.`relationName` AS `relationName`, `cc`.`relatedId` AS `relatedId`, `cc`.`relatedName` AS `relatedName`, `cc`.`projectId` AS `projectId`, `cc`.`committed` AS `committed`, `cm`.`isRelease` AS `isRelease`, `tg`.`name` AS `tag`, `br`.`name` AS `branch`, `br`.`isMain` AS `main` FROM ((((`commitedConfigView` `cc` join `commit` `cm` on(`cc`.`id` = `cm`.`id`)) left join `tag` `tg` on(`cm`.`id` = `tg`.`configurationId`)) join `configuration_branch` `cfbr` on(`cfbr`.`configurationId` = `cm`.`id`)) join `branch` `br` on(`br`.`id` = `cfbr`.`branchId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `configurationView`
--
DROP TABLE IF EXISTS `configurationView`;

CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `configurationView`  AS SELECT `wi`.`id` AS `id`, `wi`.`workItemType` AS `type`, `wi`.`name` AS `name`, `wi`.`description` AS `description`, `wi`.`created` AS `created`, `author`.`id` AS `authorId`, `author`.`name` AS `authorName`, `cpr`.`name` AS `relationName`, `related`.`id` AS `relatedId`, `related`.`name` AS `relatedName`, `c`.`projectId` AS `projectId` FROM ((((`work_item` `wi` join `configuration` `c` on(`c`.`id` = `wi`.`id`)) join `personView` `author` on(`author`.`id` = `wi`.`authorId`)) left join `configuration_person_relation` `cpr` on(`cpr`.`configurationId` = `c`.`id`)) left join `personView` `related` on(`cpr`.`personId` = `related`.`id`)) ;

-- --------------------------------------------------------

--
-- Structure for view `fieldChangeView`
--
DROP TABLE IF EXISTS `fieldChangeView`;

CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `fieldChangeView`  AS SELECT `conf`.`id` AS `id`, `conf`.`type` AS `type`, `conf`.`name` AS `name`, `conf`.`description` AS `description`, `conf`.`created` AS `created`, `conf`.`authorId` AS `authorId`, `conf`.`authorName` AS `authorName`, `conf`.`relationName` AS `relationName`, `conf`.`relatedId` AS `relatedId`, `conf`.`relatedName` AS `relatedName`, `conf`.`projectId` AS `projectId`, `wchange`.`name` AS `changeName`, `wchange`.`description` AS `changeDesc`, `item`.`id` AS `itemId`, `item`.`workItemType` AS `itemType`, `item`.`name` AS `itemName`, `item`.`description` AS `itemDesc`, `item`.`created` AS `itemCreated`, `fc`.`name` AS `field`, `fc`.`newValue` AS `newValue`, `fc`.`oldValue` AS `oldValue` FROM ((((`configurationView` `conf` left join `configuration_change` `cfc` on(`cfc`.`configurationId` = `conf`.`id`)) left join `work_item_change` `wchange` on(`wchange`.`id` = `cfc`.`changeId`)) left join `field_change` `fc` on(`fc`.`workItemChangeId` = `wchange`.`id`)) join `work_item` `item` on(`wchange`.`workItemId` = `item`.`id`)) ;

-- --------------------------------------------------------

--
-- Structure for view `personView`
--
DROP TABLE IF EXISTS `personView`;

CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `personView`  AS SELECT `p`.`id` AS `id`, `p`.`name` AS `name`, `p`.`projectId` AS `projectId` FROM `person` AS `p` ;

-- --------------------------------------------------------

--
-- Structure for view `personWithRolesView`
--
DROP TABLE IF EXISTS `personWithRolesView`;

CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `personWithRolesView`  AS SELECT `p`.`id` AS `id`, `p`.`name` AS `name`, `r`.`name` AS `role`, `rc`.`class` AS `roleClass`, `rc`.`superClass` AS `roleSuperClass`, `p`.`projectId` AS `projectId` FROM (((`person` `p` join `person_role` `pr` on(`pr`.`personId` = `p`.`id`)) join `role` `r` on(`r`.`id` = `pr`.`roleId`)) join `role_classification` `rc` on(`rc`.`id` = `r`.`classId`)) ;

-- --------------------------------------------------------

--
-- Structure for view `workUnitView`
--
DROP TABLE IF EXISTS `workUnitView`;

CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `workUnitView`  AS SELECT `wi`.`id` AS `id`, `wi`.`name` AS `name`, `wi`.`description` AS `description`, `wi`.`created` AS `created`, `wu`.`dueDate` AS `dueDate`, `wu`.`estimatedTime` AS `estimatedTime`, `wu`.`progress` AS `progress`, `wu`.`spentTime` AS `spentTime`, `wu`.`startDate` AS `startDate`, `wu`.`projectId` AS `projectId`, `author`.`id` AS `authorId`, `author`.`name` AS `authorName`, `assignee`.`id` AS `assigneeId`, `assignee`.`name` AS `assigneeName`, `ac`.`name` AS `activityName`, `ac`.`description` AS `activityDesc`, `ac`.`endDate` AS `activityEndDate`, `ac`.`startDate` AS `activityStartDate`, `it`.`name` AS `iterationName`, `it`.`description` AS `iterationDesc`, `it`.`startDate` AS `iterationStartDate`, `it`.`endDate` AS `iterationEndDate`, `it`.`created` AS `iterationCreated`, `ph`.`name` AS `phaseName`, `ph`.`description` AS `phaseDesc`, `ph`.`startDate` AS `phaseStartDate`, `ph`.`endDate` AS `phaseEndDate`, `ph`.`created` AS `phaseCreated`, `pr`.`name` AS `priorityName`, `pr`.`description` AS `priorityDesc`, `prcl`.`class` AS `prioClass`, `prcl`.`superClass` AS `prioSuperClass`, `ser`.`name` AS `severityName`, `ser`.`description` AS `severityDesc`, `sercl`.`class` AS `severityClass`, `sercl`.`superClass` AS `severitySuperClass`, `res`.`name` AS `resolutionName`, `res`.`description` AS `resolutionDescription`, `rescl`.`class` AS `resolutionClass`, `rescl`.`superClass` AS `resolutionSuperClass`, `st`.`name` AS `statusName`, `st`.`description` AS `statusDescription`, `stcl`.`class` AS `statusClass`, `stcl`.`superClass` AS `statusSuperClass`, `wt`.`name` AS `wuTypeName`, `wt`.`description` AS `wuTypeDescription`, `wtcl`.`class` AS `wuTypeClass`, `ct`.`name` AS `categoryName`, `ct`.`description` AS `categoryDesc` FROM ((((((((((((((((((`work_item` `wi` join `work_unit` `wu` on(`wu`.`id` = `wi`.`id`)) join `personView` `author` on(`wi`.`authorId` = `author`.`id`)) join `personView` `assignee` on(`assignee`.`id` = `wu`.`assigneeId`)) left join `activity` `ac` on(`wu`.`activityId` = `ac`.`id`)) left join `iteration` `it` on(`wu`.`iterationId` = `it`.`id`)) left join `phase` `ph` on(`wu`.`phaseId` = `ph`.`id`)) join `priority` `pr` on(`pr`.`id` = `wu`.`priorityId`)) join `priority_classification` `prcl` on(`prcl`.`id` = `pr`.`classId`)) join `severity` `ser` on(`ser`.`id` = `wu`.`severityId`)) join `severity_classification` `sercl` on(`sercl`.`id` = `ser`.`classId`)) left join `resolution` `res` on(`res`.`id` = `wu`.`resolutionId`)) left join `resolution_classification` `rescl` on(`rescl`.`id` = `res`.`classId`)) join `status` `st` on(`st`.`id` = `wu`.`statusId`)) join `status_classification` `stcl` on(`stcl`.`id` = `st`.`classId`)) join `wu_type` `wt` on(`wt`.`id` = `wu`.`wuTypeId`)) join `wu_type_classification` `wtcl` on(`wtcl`.`id` = `wt`.`classId`)) left join `work_unit_category` `wuc` on(`wuc`.`workUnitId` = `wu`.`id`)) left join `category` `ct` on(`ct`.`id` = `wuc`.`categoryId`)) ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
