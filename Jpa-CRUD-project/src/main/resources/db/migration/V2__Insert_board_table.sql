-- 테이블 데이터 crudtest.board:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
INSERT INTO `board` (`id`, `author`, `content`, `created_at`, `title`) VALUES
	(1, 'test user', 'content test1', '2020-02-26 02:25:12.375000', 'title test1');
/*!40000 ALTER TABLE `board` ENABLE KEYS */;