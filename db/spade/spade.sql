-- phpMyAdmin SQL Dump
-- version 5.0.4deb2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 10, 2022 at 05:20 PM
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
CREATE DATABASE IF NOT EXISTS `spade` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `spade`;

-- --------------------------------------------------------

--
-- Table structure for table `activity`
--

CREATE TABLE `activity` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `superProjectId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `artifact`
--

CREATE TABLE `artifact` (
  `artifactClass` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mimeType` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `size` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `branch`
--

CREATE TABLE `branch` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isMain` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `projectInstanceId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `commit`
--

CREATE TABLE `commit` (
  `identifier` varchar(7) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isRelease` bit(1) NOT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `committed_configuration`
--

CREATE TABLE `committed_configuration` (
  `committed` datetime DEFAULT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `competency`
--

CREATE TABLE `competency` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `configuration`
--

CREATE TABLE `configuration` (
  `id` bigint(20) NOT NULL,
  `projectId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `configuration_branch`
--

CREATE TABLE `configuration_branch` (
  `configurationId` bigint(20) NOT NULL,
  `branchId` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `configuration_change`
--

CREATE TABLE `configuration_change` (
  `configurationId` bigint(20) NOT NULL,
  `changeId` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `configuration_person_relation`
--

CREATE TABLE `configuration_person_relation` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `personId` bigint(20) DEFAULT NULL,
  `configurationId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `criterion`
--

CREATE TABLE `criterion` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `field_change`
--

CREATE TABLE `field_change` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `newValue` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `oldValue` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `workItemChangeId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `group_member`
--

CREATE TABLE `group_member` (
  `groupId` bigint(20) NOT NULL,
  `memberId` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `identity`
--

CREATE TABLE `identity` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `personId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `iteration`
--

CREATE TABLE `iteration` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `superProjectId` bigint(20) DEFAULT NULL,
  `configurationId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `milestone`
--

CREATE TABLE `milestone` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `milestone_criterion`
--

CREATE TABLE `milestone_criterion` (
  `milestoneId` bigint(20) NOT NULL,
  `criterionId` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `people_group`
--

CREATE TABLE `people_group` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `projectInstanceId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `person`
--

CREATE TABLE `person` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `projectId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `person_competency`
--

CREATE TABLE `person_competency` (
  `personId` bigint(20) NOT NULL,
  `competencyId` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `person_role`
--

CREATE TABLE `person_role` (
  `personId` bigint(20) NOT NULL,
  `roleId` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `phase`
--

CREATE TABLE `phase` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `superProjectId` bigint(20) DEFAULT NULL,
  `configurationId` bigint(20) DEFAULT NULL,
  `milestoneId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `priority`
--

CREATE TABLE `priority` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `classId` bigint(20) DEFAULT NULL,
  `projectInstanceId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `priority_classification`
--

CREATE TABLE `priority_classification` (
  `id` bigint(20) NOT NULL,
  `class` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `superClass` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `priority_classification`
--

INSERT INTO `priority_classification` (`id`, `class`, `superClass`) VALUES
(1, 'UNASSIGNED', 'UNASSIGNED'),
(2, 'LOWEST', 'LOW'),
(3, 'LOW', 'LOW'),
(4, 'NORMAL', 'NORMAL'),
(5, 'HIGH', 'HIGH'),
(6, 'HIGHEST', 'HIGH');

-- --------------------------------------------------------

--
-- Table structure for table `program`
--

CREATE TABLE `program` (
  `programClass` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE `project` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `superProjectId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `project_instance`
--

CREATE TABLE `project_instance` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `projectId` bigint(20) DEFAULT NULL,
  `toolInstanceId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `relation`
--

CREATE TABLE `relation` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `classId` bigint(20) DEFAULT NULL,
  `projectInstanceId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `relation_classification`
--

CREATE TABLE `relation_classification` (
  `id` bigint(20) NOT NULL,
  `class` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `superClass` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `relation_classification`
--

INSERT INTO `relation_classification` (`id`, `class`, `superClass`) VALUES
(1, 'UNASSIGNED', 'UNASSIGNED'),
(2, 'DUPLICATES', 'SIMILARITY'),
(3, 'DUPLICATEDBY', 'SIMILARITY'),
(4, 'BLOCKS', 'TEMPORAL'),
(5, 'BLOCKEDBY', 'TEMPORAL'),
(6, 'RELATESTO', 'GENERAL'),
(7, 'PRECEDES', 'TEMPORAL'),
(8, 'FOLLOWS', 'TEMPORAL'),
(9, 'COPIEDFROM', 'SIMILARITY'),
(10, 'COPIEDBY', 'SIMILARITY'),
(11, 'CHILDOF', 'HIERARCHICAL'),
(12, 'PARENTOF', 'HIERARCHICAL'),
(13, 'CAUSES', 'CAUSAL'),
(14, 'CAUSEDBY', 'CAUSAL'),
(15, 'RESOLVES', 'CAUSAL'),
(16, 'RESOLVEDBY', 'CAUSAL');

-- --------------------------------------------------------

--
-- Table structure for table `resolution`
--

CREATE TABLE `resolution` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `classId` bigint(20) DEFAULT NULL,
  `projectInstanceId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `resolution_classification`
--

CREATE TABLE `resolution_classification` (
  `id` bigint(20) NOT NULL,
  `class` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `superClass` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `resolution_classification`
--

INSERT INTO `resolution_classification` (`id`, `class`, `superClass`) VALUES
(1, 'UNASSIGNED', 'UNASSIGNED'),
(2, 'DUPLICATE', 'FINISHED'),
(3, 'INVALID', 'FINISHED'),
(4, 'WONTFIX', 'FINISHED'),
(5, 'WORKSASDESIGNED', 'FINISHED'),
(6, 'FIXED', 'FINISHED'),
(7, 'FINISHED', 'FINISHED'),
(8, 'INCOMPLETE', 'UNFINISHED'),
(9, 'WORKSFORME', 'UNFINISHED'),
(10, 'UNFINISHED', 'UNFINISHED');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `classId` bigint(20) DEFAULT NULL,
  `projectInstanceId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `role_classification`
--

CREATE TABLE `role_classification` (
  `id` bigint(20) NOT NULL,
  `class` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `superClass` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `role_classification`
--

INSERT INTO `role_classification` (`id`, `class`, `superClass`) VALUES
(1, 'UNASSIGNED', 'UNASSIGNED'),
(2, 'NONMEMBER', 'NONMEMBER'),
(3, 'MENTOR', 'STAKEHOLDER'),
(4, 'STAKEHOLDER', 'STAKEHOLDER'),
(5, 'PROJECTMANAGER', 'MANAGEMENT'),
(6, 'TEAMMEMBER', 'TEAMMEMBER'),
(7, 'ANALYST', 'TEAMMEMBER'),
(8, 'DESIGNER', 'TEAMMEMBER'),
(9, 'DEVELOPER', 'TEAMMEMBER'),
(10, 'TESTER', 'TEAMMEMBER'),
(11, 'DOCUMENTER', 'TEAMMEMBER');

-- --------------------------------------------------------

--
-- Table structure for table `severity`
--

CREATE TABLE `severity` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `classId` bigint(20) DEFAULT NULL,
  `projectInstanceId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `severity_classification`
--

CREATE TABLE `severity_classification` (
  `id` bigint(20) NOT NULL,
  `class` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `superClass` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `severity_classification`
--

INSERT INTO `severity_classification` (`id`, `class`, `superClass`) VALUES
(1, 'UNASSIGNED', 'UNASSIGNED'),
(2, 'TRIVIAL', 'MINOR'),
(3, 'MINOR', 'MINOR'),
(4, 'NORMAL', 'NORMAL'),
(5, 'MAJOR', 'MAJOR'),
(6, 'CRITICAL', 'MAJOR');

-- --------------------------------------------------------

--
-- Table structure for table `status`
--

CREATE TABLE `status` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `classId` bigint(20) DEFAULT NULL,
  `projectInstanceId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `status_classification`
--

CREATE TABLE `status_classification` (
  `id` bigint(20) NOT NULL,
  `class` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `superClass` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `status_classification`
--

INSERT INTO `status_classification` (`id`, `class`, `superClass`) VALUES
(1, 'UNASSIGNED', 'UNASSIGNED'),
(2, 'NEW', 'OPEN'),
(3, 'OPEN', 'OPEN'),
(4, 'ACCEPTED', 'OPEN'),
(5, 'INPROGRESS', 'OPEN'),
(6, 'RESOLVED', 'OPEN'),
(7, 'VERIFIED', 'OPEN'),
(8, 'DONE', 'CLOSED'),
(9, 'CLOSED', 'CLOSED'),
(10, 'INVALID', 'CLOSED'),
(11, 'DELETED', 'CLOSED');

-- --------------------------------------------------------

--
-- Table structure for table `tag`
--

CREATE TABLE `tag` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `configurationId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tool_instance`
--

CREATE TABLE `tool_instance` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tool` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `version` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `work_item`
--

CREATE TABLE `work_item` (
  `workItemType` varchar(31) COLLATE utf8_unicode_ci NOT NULL,
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `authorId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `work_item_change`
--

CREATE TABLE `work_item_change` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `workItemId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `work_item_relation`
--

CREATE TABLE `work_item_relation` (
  `id` bigint(20) NOT NULL,
  `rightItemId` bigint(20) DEFAULT NULL,
  `relationId` bigint(20) DEFAULT NULL,
  `leftItemId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `work_unit`
--

CREATE TABLE `work_unit` (
  `dueDate` date DEFAULT NULL,
  `estimatedTime` double NOT NULL,
  `number` int(11) NOT NULL,
  `progress` int(11) NOT NULL,
  `spentTime` double NOT NULL,
  `startDate` date DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `activityId` bigint(20) DEFAULT NULL,
  `assigneeId` bigint(20) DEFAULT NULL,
  `iterationId` bigint(20) DEFAULT NULL,
  `phaseId` bigint(20) DEFAULT NULL,
  `priorityId` bigint(20) DEFAULT NULL,
  `resolutionId` bigint(20) DEFAULT NULL,
  `severityId` bigint(20) DEFAULT NULL,
  `statusId` bigint(20) DEFAULT NULL,
  `wuTypeId` bigint(20) DEFAULT NULL,
  `projectId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `work_unit_category`
--

CREATE TABLE `work_unit_category` (
  `workUnitId` bigint(20) NOT NULL,
  `categoryId` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `wu_type`
--

CREATE TABLE `wu_type` (
  `id` bigint(20) NOT NULL,
  `externalId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `classId` bigint(20) DEFAULT NULL,
  `projectInstanceId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `wu_type_classification`
--

CREATE TABLE `wu_type_classification` (
  `id` bigint(20) NOT NULL,
  `class` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `wu_type_classification`
--

INSERT INTO `wu_type_classification` (`id`, `class`) VALUES
(1, 'UNASSIGNED'),
(2, 'BUG'),
(3, 'ENHANCEMENT'),
(4, 'FEATURE'),
(5, 'TASK');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activity`
--
ALTER TABLE `activity`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_blqrry9wsm0foxy0mg00a0epk` (`superProjectId`);

--
-- Indexes for table `artifact`
--
ALTER TABLE `artifact`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `branch`
--
ALTER TABLE `branch`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_cpikjj79a9qmnxmo5unps4hsw` (`projectInstanceId`);

--
-- Indexes for table `commit`
--
ALTER TABLE `commit`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `committed_configuration`
--
ALTER TABLE `committed_configuration`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `competency`
--
ALTER TABLE `competency`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `configuration`
--
ALTER TABLE `configuration`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_o7bmirglj65sjrcym8a1ywgwc` (`projectId`);

--
-- Indexes for table `configuration_branch`
--
ALTER TABLE `configuration_branch`
  ADD KEY `FK_kq1ppnqu72in7ciaiv0qfm7gq` (`branchId`),
  ADD KEY `FK_bvbx76vbqrs4wkix8a4l64mgk` (`configurationId`);

--
-- Indexes for table `configuration_change`
--
ALTER TABLE `configuration_change`
  ADD UNIQUE KEY `UK_2vr3gl6l9t320r67yfspg8e16` (`changeId`),
  ADD KEY `FK_b11yrb5xl6ea5lceca8r6eyaq` (`configurationId`);

--
-- Indexes for table `configuration_person_relation`
--
ALTER TABLE `configuration_person_relation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_bannhsxpvu262ewrp6aval67i` (`personId`),
  ADD KEY `FK_miwxanxtv7gt3knvsflwncix7` (`configurationId`);

--
-- Indexes for table `criterion`
--
ALTER TABLE `criterion`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `field_change`
--
ALTER TABLE `field_change`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_mpcn64kubumdcj69467wdexg6` (`workItemChangeId`);

--
-- Indexes for table `group_member`
--
ALTER TABLE `group_member`
  ADD KEY `FK_67y4ufvw9p8w6mt4cp7bwa972` (`memberId`),
  ADD KEY `FK_g639kt1nekbaykgg0pwy8ips8` (`groupId`);

--
-- Indexes for table `identity`
--
ALTER TABLE `identity`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_ocuj7jp17fw0qcow4o3f8e8o0` (`personId`);

--
-- Indexes for table `iteration`
--
ALTER TABLE `iteration`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_55okl4mwia1vo7n8q6dsbfr55` (`superProjectId`),
  ADD KEY `FK_bid9cxxcrg3oufp2wc33j1h1e` (`configurationId`);

--
-- Indexes for table `milestone`
--
ALTER TABLE `milestone`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `milestone_criterion`
--
ALTER TABLE `milestone_criterion`
  ADD KEY `FK_j65caq74d17bhsly89kylc3jk` (`criterionId`),
  ADD KEY `FK_hahtmhvat46jgvqn41s98dxdl` (`milestoneId`);

--
-- Indexes for table `people_group`
--
ALTER TABLE `people_group`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_pif918vt32wbakh9vwaye6ib4` (`projectInstanceId`);

--
-- Indexes for table `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_nh5ixvgwp0pxk9k1aafmgaltj` (`projectId`);

--
-- Indexes for table `person_competency`
--
ALTER TABLE `person_competency`
  ADD KEY `FK_a8kjcx0bj27oxbni693verap4` (`competencyId`),
  ADD KEY `FK_a8x0e7bl7yfoab8d2asvki1c4` (`personId`);

--
-- Indexes for table `person_role`
--
ALTER TABLE `person_role`
  ADD KEY `FK_a7r3tdncdaslk1knqt5hue1hq` (`roleId`),
  ADD KEY `FK_cdyyqtr37aeahm3pe162pdgst` (`personId`);

--
-- Indexes for table `phase`
--
ALTER TABLE `phase`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_ip37oqdl21wk9oqbgm8ii2eop` (`superProjectId`),
  ADD KEY `FK_bo7xp1rl1h46kxd0xvk9d9cif` (`configurationId`),
  ADD KEY `FK_be0977ugg5npl8dxd87mcn7n5` (`milestoneId`);

--
-- Indexes for table `priority`
--
ALTER TABLE `priority`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_qpy5gsqj4wu6lavuh3biip56v` (`classId`),
  ADD KEY `FK_6w6jf1op7pi7pb2xojyg6jakq` (`projectInstanceId`);

--
-- Indexes for table `priority_classification`
--
ALTER TABLE `priority_classification`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `program`
--
ALTER TABLE `program`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_s45m7qvl6bbm8be5olfwc9n0r` (`superProjectId`);

--
-- Indexes for table `project_instance`
--
ALTER TABLE `project_instance`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_d16nrgrtg46ln2r8qfc4kqaye` (`projectId`),
  ADD KEY `FK_l9bmcaixavuekut7gfi5up1dc` (`toolInstanceId`);

--
-- Indexes for table `relation`
--
ALTER TABLE `relation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_ggp4ppf3qtr9gtpo1je8p8who` (`classId`),
  ADD KEY `FK_lqcwy0o733b26w312lmp0nliw` (`projectInstanceId`);

--
-- Indexes for table `relation_classification`
--
ALTER TABLE `relation_classification`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `resolution`
--
ALTER TABLE `resolution`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_au7be5w5c1wmlvr1sy0s0bro7` (`classId`),
  ADD KEY `FK_74tplny25hv0k9wsji0mb0u94` (`projectInstanceId`);

--
-- Indexes for table `resolution_classification`
--
ALTER TABLE `resolution_classification`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_g50l5oh7otkun3cwrkx32o1el` (`classId`),
  ADD KEY `FK_nbulk1gvi6k2hi1s21dcbn973` (`projectInstanceId`);

--
-- Indexes for table `role_classification`
--
ALTER TABLE `role_classification`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `severity`
--
ALTER TABLE `severity`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_ee5mdcxuy7b3jl1tg4hstv7av` (`classId`),
  ADD KEY `FK_9155uwikqouv9vahj5y02jgq0` (`projectInstanceId`);

--
-- Indexes for table `severity_classification`
--
ALTER TABLE `severity_classification`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `status`
--
ALTER TABLE `status`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_4ipxdl1fixo7gorjnjf1vtebu` (`classId`),
  ADD KEY `FK_m16n7o8v21c1x7l9bimxfcaiu` (`projectInstanceId`);

--
-- Indexes for table `status_classification`
--
ALTER TABLE `status_classification`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tag`
--
ALTER TABLE `tag`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_m07et5iue1u1mebbdvxi40430` (`configurationId`);

--
-- Indexes for table `tool_instance`
--
ALTER TABLE `tool_instance`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `work_item`
--
ALTER TABLE `work_item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_grormlstxeqy8q0kuiul86oti` (`authorId`);

--
-- Indexes for table `work_item_change`
--
ALTER TABLE `work_item_change`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_eqs2goyddmd6gps1ua15jn4y7` (`workItemId`);

--
-- Indexes for table `work_item_relation`
--
ALTER TABLE `work_item_relation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_3ggc732qs69geici6kmfq8tir` (`rightItemId`),
  ADD KEY `FK_65qc19uvlwf5kk99nrqn4g4x4` (`relationId`),
  ADD KEY `FK_7o6onl1fkxry7lunkllomq6py` (`leftItemId`);

--
-- Indexes for table `work_unit`
--
ALTER TABLE `work_unit`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_hapc5vkbtxaottwycluaohfhk` (`activityId`),
  ADD KEY `FK_bg8wdau2p07tbijinx445xyxc` (`assigneeId`),
  ADD KEY `FK_dw94wosdhq0lf0j1eebs7c096` (`iterationId`),
  ADD KEY `FK_kpa354soynck8d9v16bx29wqr` (`phaseId`),
  ADD KEY `FK_gub58igbd2kmb4swfs16ed7te` (`priorityId`),
  ADD KEY `FK_8pjjfib3dfnt6wdkqvs6w2ohk` (`resolutionId`),
  ADD KEY `FK_hdtowjwpdbi5iq0iqkurnsicv` (`severityId`),
  ADD KEY `FK_332c0iln6b790nmcowr7li152` (`statusId`),
  ADD KEY `FK_l7yf2722dfp6wu8mcvl7lu024` (`wuTypeId`),
  ADD KEY `FK_aui0kjlc2ileby8e4toik9pq4` (`projectId`);

--
-- Indexes for table `work_unit_category`
--
ALTER TABLE `work_unit_category`
  ADD KEY `FK_gikg1399mwbk4aqit3bl7t7mc` (`categoryId`),
  ADD KEY `FK_9esb50fgqu304q0paecd3j0p1` (`workUnitId`);

--
-- Indexes for table `wu_type`
--
ALTER TABLE `wu_type`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_kltfeqj1it4r8vgucs9uwg578` (`classId`),
  ADD KEY `FK_kdcm0gbg2748gs96dj6tadc6w` (`projectInstanceId`);

--
-- Indexes for table `wu_type_classification`
--
ALTER TABLE `wu_type_classification`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activity`
--
ALTER TABLE `activity`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `branch`
--
ALTER TABLE `branch`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `competency`
--
ALTER TABLE `competency`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `configuration_person_relation`
--
ALTER TABLE `configuration_person_relation`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `criterion`
--
ALTER TABLE `criterion`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `field_change`
--
ALTER TABLE `field_change`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `identity`
--
ALTER TABLE `identity`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `iteration`
--
ALTER TABLE `iteration`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `milestone`
--
ALTER TABLE `milestone`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `people_group`
--
ALTER TABLE `people_group`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `person`
--
ALTER TABLE `person`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `phase`
--
ALTER TABLE `phase`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `priority`
--
ALTER TABLE `priority`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `priority_classification`
--
ALTER TABLE `priority_classification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `project`
--
ALTER TABLE `project`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `project_instance`
--
ALTER TABLE `project_instance`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `relation`
--
ALTER TABLE `relation`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `relation_classification`
--
ALTER TABLE `relation_classification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `resolution`
--
ALTER TABLE `resolution`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `resolution_classification`
--
ALTER TABLE `resolution_classification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `role_classification`
--
ALTER TABLE `role_classification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `severity`
--
ALTER TABLE `severity`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `severity_classification`
--
ALTER TABLE `severity_classification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `status`
--
ALTER TABLE `status`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `status_classification`
--
ALTER TABLE `status_classification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `tag`
--
ALTER TABLE `tag`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tool_instance`
--
ALTER TABLE `tool_instance`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `work_item`
--
ALTER TABLE `work_item`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `work_item_change`
--
ALTER TABLE `work_item_change`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `work_item_relation`
--
ALTER TABLE `work_item_relation`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `wu_type`
--
ALTER TABLE `wu_type`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `wu_type_classification`
--
ALTER TABLE `wu_type_classification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `activity`
--
ALTER TABLE `activity`
  ADD CONSTRAINT `FK_blqrry9wsm0foxy0mg00a0epk` FOREIGN KEY (`superProjectId`) REFERENCES `project` (`id`);

--
-- Constraints for table `artifact`
--
ALTER TABLE `artifact`
  ADD CONSTRAINT `FK_rw4j86aih7ao55ljy9bhaexgj` FOREIGN KEY (`id`) REFERENCES `work_item` (`id`);

--
-- Constraints for table `category`
--
ALTER TABLE `category`
  ADD CONSTRAINT `FK_cpikjj79a9qmnxmo5unps4hsw` FOREIGN KEY (`projectInstanceId`) REFERENCES `project_instance` (`id`);

--
-- Constraints for table `commit`
--
ALTER TABLE `commit`
  ADD CONSTRAINT `FK_nf03tfesxrvrfrkhqhykkjaxo` FOREIGN KEY (`id`) REFERENCES `committed_configuration` (`id`);

--
-- Constraints for table `committed_configuration`
--
ALTER TABLE `committed_configuration`
  ADD CONSTRAINT `FK_sfu3uuxhnjmgbca2pwkwv9iyh` FOREIGN KEY (`id`) REFERENCES `configuration` (`id`);

--
-- Constraints for table `configuration`
--
ALTER TABLE `configuration`
  ADD CONSTRAINT `FK_o7bmirglj65sjrcym8a1ywgwc` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`),
  ADD CONSTRAINT `FK_q1y4xb3dkmkvadurm574ago0j` FOREIGN KEY (`id`) REFERENCES `work_item` (`id`);

--
-- Constraints for table `configuration_branch`
--
ALTER TABLE `configuration_branch`
  ADD CONSTRAINT `FK_bvbx76vbqrs4wkix8a4l64mgk` FOREIGN KEY (`configurationId`) REFERENCES `commit` (`id`),
  ADD CONSTRAINT `FK_kq1ppnqu72in7ciaiv0qfm7gq` FOREIGN KEY (`branchId`) REFERENCES `branch` (`id`);

--
-- Constraints for table `configuration_change`
--
ALTER TABLE `configuration_change`
  ADD CONSTRAINT `FK_2vr3gl6l9t320r67yfspg8e16` FOREIGN KEY (`changeId`) REFERENCES `work_item_change` (`id`),
  ADD CONSTRAINT `FK_b11yrb5xl6ea5lceca8r6eyaq` FOREIGN KEY (`configurationId`) REFERENCES `configuration` (`id`);

--
-- Constraints for table `configuration_person_relation`
--
ALTER TABLE `configuration_person_relation`
  ADD CONSTRAINT `FK_bannhsxpvu262ewrp6aval67i` FOREIGN KEY (`personId`) REFERENCES `person` (`id`),
  ADD CONSTRAINT `FK_miwxanxtv7gt3knvsflwncix7` FOREIGN KEY (`configurationId`) REFERENCES `commit` (`id`);

--
-- Constraints for table `field_change`
--
ALTER TABLE `field_change`
  ADD CONSTRAINT `FK_mpcn64kubumdcj69467wdexg6` FOREIGN KEY (`workItemChangeId`) REFERENCES `work_item_change` (`id`);

--
-- Constraints for table `group_member`
--
ALTER TABLE `group_member`
  ADD CONSTRAINT `FK_67y4ufvw9p8w6mt4cp7bwa972` FOREIGN KEY (`memberId`) REFERENCES `person` (`id`),
  ADD CONSTRAINT `FK_g639kt1nekbaykgg0pwy8ips8` FOREIGN KEY (`groupId`) REFERENCES `people_group` (`id`);

--
-- Constraints for table `identity`
--
ALTER TABLE `identity`
  ADD CONSTRAINT `FK_ocuj7jp17fw0qcow4o3f8e8o0` FOREIGN KEY (`personId`) REFERENCES `person` (`id`);

--
-- Constraints for table `iteration`
--
ALTER TABLE `iteration`
  ADD CONSTRAINT `FK_55okl4mwia1vo7n8q6dsbfr55` FOREIGN KEY (`superProjectId`) REFERENCES `project` (`id`),
  ADD CONSTRAINT `FK_bid9cxxcrg3oufp2wc33j1h1e` FOREIGN KEY (`configurationId`) REFERENCES `commit` (`id`);

--
-- Constraints for table `milestone_criterion`
--
ALTER TABLE `milestone_criterion`
  ADD CONSTRAINT `FK_hahtmhvat46jgvqn41s98dxdl` FOREIGN KEY (`milestoneId`) REFERENCES `milestone` (`id`),
  ADD CONSTRAINT `FK_j65caq74d17bhsly89kylc3jk` FOREIGN KEY (`criterionId`) REFERENCES `criterion` (`id`);

--
-- Constraints for table `people_group`
--
ALTER TABLE `people_group`
  ADD CONSTRAINT `FK_pif918vt32wbakh9vwaye6ib4` FOREIGN KEY (`projectInstanceId`) REFERENCES `project_instance` (`id`);

--
-- Constraints for table `person`
--
ALTER TABLE `person`
  ADD CONSTRAINT `FK_nh5ixvgwp0pxk9k1aafmgaltj` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`);

--
-- Constraints for table `person_competency`
--
ALTER TABLE `person_competency`
  ADD CONSTRAINT `FK_a8kjcx0bj27oxbni693verap4` FOREIGN KEY (`competencyId`) REFERENCES `competency` (`id`),
  ADD CONSTRAINT `FK_a8x0e7bl7yfoab8d2asvki1c4` FOREIGN KEY (`personId`) REFERENCES `person` (`id`);

--
-- Constraints for table `person_role`
--
ALTER TABLE `person_role`
  ADD CONSTRAINT `FK_a7r3tdncdaslk1knqt5hue1hq` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `FK_cdyyqtr37aeahm3pe162pdgst` FOREIGN KEY (`personId`) REFERENCES `person` (`id`);

--
-- Constraints for table `phase`
--
ALTER TABLE `phase`
  ADD CONSTRAINT `FK_be0977ugg5npl8dxd87mcn7n5` FOREIGN KEY (`milestoneId`) REFERENCES `milestone` (`id`),
  ADD CONSTRAINT `FK_bo7xp1rl1h46kxd0xvk9d9cif` FOREIGN KEY (`configurationId`) REFERENCES `commit` (`id`),
  ADD CONSTRAINT `FK_ip37oqdl21wk9oqbgm8ii2eop` FOREIGN KEY (`superProjectId`) REFERENCES `project` (`id`);

--
-- Constraints for table `priority`
--
ALTER TABLE `priority`
  ADD CONSTRAINT `FK_6w6jf1op7pi7pb2xojyg6jakq` FOREIGN KEY (`projectInstanceId`) REFERENCES `project_instance` (`id`),
  ADD CONSTRAINT `FK_qpy5gsqj4wu6lavuh3biip56v` FOREIGN KEY (`classId`) REFERENCES `priority_classification` (`id`);

--
-- Constraints for table `program`
--
ALTER TABLE `program`
  ADD CONSTRAINT `FK_i9n77wvkvre5cxu7eiqsfqup9` FOREIGN KEY (`id`) REFERENCES `project` (`id`);

--
-- Constraints for table `project`
--
ALTER TABLE `project`
  ADD CONSTRAINT `FK_s45m7qvl6bbm8be5olfwc9n0r` FOREIGN KEY (`superProjectId`) REFERENCES `project` (`id`);

--
-- Constraints for table `project_instance`
--
ALTER TABLE `project_instance`
  ADD CONSTRAINT `FK_d16nrgrtg46ln2r8qfc4kqaye` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`),
  ADD CONSTRAINT `FK_l9bmcaixavuekut7gfi5up1dc` FOREIGN KEY (`toolInstanceId`) REFERENCES `tool_instance` (`id`);

--
-- Constraints for table `relation`
--
ALTER TABLE `relation`
  ADD CONSTRAINT `FK_ggp4ppf3qtr9gtpo1je8p8who` FOREIGN KEY (`classId`) REFERENCES `relation_classification` (`id`),
  ADD CONSTRAINT `FK_lqcwy0o733b26w312lmp0nliw` FOREIGN KEY (`projectInstanceId`) REFERENCES `project_instance` (`id`);

--
-- Constraints for table `resolution`
--
ALTER TABLE `resolution`
  ADD CONSTRAINT `FK_74tplny25hv0k9wsji0mb0u94` FOREIGN KEY (`projectInstanceId`) REFERENCES `project_instance` (`id`),
  ADD CONSTRAINT `FK_au7be5w5c1wmlvr1sy0s0bro7` FOREIGN KEY (`classId`) REFERENCES `resolution_classification` (`id`);

--
-- Constraints for table `role`
--
ALTER TABLE `role`
  ADD CONSTRAINT `FK_g50l5oh7otkun3cwrkx32o1el` FOREIGN KEY (`classId`) REFERENCES `role_classification` (`id`),
  ADD CONSTRAINT `FK_nbulk1gvi6k2hi1s21dcbn973` FOREIGN KEY (`projectInstanceId`) REFERENCES `project_instance` (`id`);

--
-- Constraints for table `severity`
--
ALTER TABLE `severity`
  ADD CONSTRAINT `FK_9155uwikqouv9vahj5y02jgq0` FOREIGN KEY (`projectInstanceId`) REFERENCES `project_instance` (`id`),
  ADD CONSTRAINT `FK_ee5mdcxuy7b3jl1tg4hstv7av` FOREIGN KEY (`classId`) REFERENCES `severity_classification` (`id`);

--
-- Constraints for table `status`
--
ALTER TABLE `status`
  ADD CONSTRAINT `FK_4ipxdl1fixo7gorjnjf1vtebu` FOREIGN KEY (`classId`) REFERENCES `status_classification` (`id`),
  ADD CONSTRAINT `FK_m16n7o8v21c1x7l9bimxfcaiu` FOREIGN KEY (`projectInstanceId`) REFERENCES `project_instance` (`id`);

--
-- Constraints for table `tag`
--
ALTER TABLE `tag`
  ADD CONSTRAINT `FK_m07et5iue1u1mebbdvxi40430` FOREIGN KEY (`configurationId`) REFERENCES `commit` (`id`);

--
-- Constraints for table `work_item`
--
ALTER TABLE `work_item`
  ADD CONSTRAINT `FK_grormlstxeqy8q0kuiul86oti` FOREIGN KEY (`authorId`) REFERENCES `person` (`id`);

--
-- Constraints for table `work_item_change`
--
ALTER TABLE `work_item_change`
  ADD CONSTRAINT `FK_eqs2goyddmd6gps1ua15jn4y7` FOREIGN KEY (`workItemId`) REFERENCES `work_item` (`id`);

--
-- Constraints for table `work_item_relation`
--
ALTER TABLE `work_item_relation`
  ADD CONSTRAINT `FK_3ggc732qs69geici6kmfq8tir` FOREIGN KEY (`rightItemId`) REFERENCES `work_item` (`id`),
  ADD CONSTRAINT `FK_65qc19uvlwf5kk99nrqn4g4x4` FOREIGN KEY (`relationId`) REFERENCES `relation` (`id`),
  ADD CONSTRAINT `FK_7o6onl1fkxry7lunkllomq6py` FOREIGN KEY (`leftItemId`) REFERENCES `work_item` (`id`);

--
-- Constraints for table `work_unit`
--
ALTER TABLE `work_unit`
  ADD CONSTRAINT `FK_332c0iln6b790nmcowr7li152` FOREIGN KEY (`statusId`) REFERENCES `status` (`id`),
  ADD CONSTRAINT `FK_8pjjfib3dfnt6wdkqvs6w2ohk` FOREIGN KEY (`resolutionId`) REFERENCES `resolution` (`id`),
  ADD CONSTRAINT `FK_aui0kjlc2ileby8e4toik9pq4` FOREIGN KEY (`projectId`) REFERENCES `project` (`id`),
  ADD CONSTRAINT `FK_bg8wdau2p07tbijinx445xyxc` FOREIGN KEY (`assigneeId`) REFERENCES `person` (`id`),
  ADD CONSTRAINT `FK_dw94wosdhq0lf0j1eebs7c096` FOREIGN KEY (`iterationId`) REFERENCES `iteration` (`id`),
  ADD CONSTRAINT `FK_gub58igbd2kmb4swfs16ed7te` FOREIGN KEY (`priorityId`) REFERENCES `priority` (`id`),
  ADD CONSTRAINT `FK_hapc5vkbtxaottwycluaohfhk` FOREIGN KEY (`activityId`) REFERENCES `activity` (`id`),
  ADD CONSTRAINT `FK_hdtowjwpdbi5iq0iqkurnsicv` FOREIGN KEY (`severityId`) REFERENCES `severity` (`id`),
  ADD CONSTRAINT `FK_kpa354soynck8d9v16bx29wqr` FOREIGN KEY (`phaseId`) REFERENCES `phase` (`id`),
  ADD CONSTRAINT `FK_l7yf2722dfp6wu8mcvl7lu024` FOREIGN KEY (`wuTypeId`) REFERENCES `wu_type` (`id`),
  ADD CONSTRAINT `FK_qhiwrpysllugj9omo1r2faqt4` FOREIGN KEY (`id`) REFERENCES `work_item` (`id`);

--
-- Constraints for table `work_unit_category`
--
ALTER TABLE `work_unit_category`
  ADD CONSTRAINT `FK_9esb50fgqu304q0paecd3j0p1` FOREIGN KEY (`workUnitId`) REFERENCES `work_unit` (`id`),
  ADD CONSTRAINT `FK_gikg1399mwbk4aqit3bl7t7mc` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`);

--
-- Constraints for table `wu_type`
--
ALTER TABLE `wu_type`
  ADD CONSTRAINT `FK_kdcm0gbg2748gs96dj6tadc6w` FOREIGN KEY (`projectInstanceId`) REFERENCES `project_instance` (`id`),
  ADD CONSTRAINT `FK_kltfeqj1it4r8vgucs9uwg578` FOREIGN KEY (`classId`) REFERENCES `wu_type_classification` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
