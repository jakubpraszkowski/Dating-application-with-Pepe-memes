-- phpMyAdmin SQL Dump
-- version 5.1.1deb5ubuntu1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Czas generowania: 07 Maj 2023, 18:28
-- Wersja serwera: 8.0.32-0ubuntu0.22.04.2
-- Wersja PHP: 8.1.2-1ubuntu2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `meme_dating`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `categories`
--

CREATE TABLE `categories` (
  `cat_id` int NOT NULL,
  `title` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Zrzut danych tabeli `categories`
--

INSERT INTO `categories` (`cat_id`, `title`) VALUES
(1, 'IT'),
(2, 'Political'),
(3, 'Anime'),
(4, 'Religion'),
(5, 'Dark Humor');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `memes`
--

CREATE TABLE `memes` (
  `m_id` int NOT NULL,
  `url` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `title` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `cat_id` int NOT NULL,
  `u_id` int NOT NULL,
  `add_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Zrzut danych tabeli `memes`
--

INSERT INTO `memes` (`m_id`, `url`, `title`, `cat_id`, `u_id`, `add_date`) VALUES
(1, 'https://static.demilked.com/wp-content/uploads/2021/07/60ed37b2415f9-it-rage-comics-memes-reddit-60e6e9004b503__700.jpg', 'some title', 1, 6, '2023-05-01'),
(2, 'https://s7280.pcdn.co/wp-content/uploads/2019/07/IT-Manager-Pro-Tip-Meme-300x288.jpg.optimal.jpg', 'nice meme', 1, 1, '2023-05-01'),
(3, 'https://miro.medium.com/v2/resize:fit:1400/0*z1mm6izqSeDiKukb', 'xdddd', 1, 3, '2023-05-16'),
(4, 'https://pbs.twimg.com/media/EdXGQguXgAA6VlX.jpg', 'nice meme 2', 1, 6, '2023-05-26'),
(5, 'https://i.redd.it/h123vo12rof81.jpg', 'hiiiiii', 1, 1, '2023-05-06'),
(6, 'https://programmerhumor.io/wp-content/uploads/2023/02/programmerhumor-io-programming-memes-1303766a3da9d2a-758x746.png', 'UwU', 1, 3, '2023-05-09'),
(7, 'https://media.discordapp.net/attachments/1088112024577392650/1104008181329444895/345183001_972827300738507_1259883296693983383_n.png?width=514&height=676', 'that so funny', 2, 1, '2023-05-03'),
(8, 'https://media.discordapp.net/attachments/1088112024577392650/1101827983951532102/IMG_8578.jpg', 'true', 2, 6, '2023-05-03'),
(9, 'https://media.discordapp.net/attachments/1088112024577392650/1101801933901148160/FB_IMG_1682424598635.png', 'another title', 2, 1, '2023-05-03'),
(10, 'https://media.discordapp.net/attachments/1088112024577392650/1100836282055270481/FEdYBQsXIAA3ezR.jpg', 'omg', 2, 1, '2023-05-03'),
(11, 'https://media.discordapp.net/attachments/1088112024577392650/1100710932566519838/47sfvsglovua1.webp?width=601&height=676', 'funny', 2, 6, '2023-05-03'),
(12, 'https://media.discordapp.net/attachments/1088112024577392650/1099780834514444450/image.png?width=1215&height=676', 'cringe', 2, 3, '2023-05-03');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `meme_likes`
--

CREATE TABLE `meme_likes` (
  `like_id` int NOT NULL,
  `m_id` int NOT NULL,
  `u_id` int NOT NULL,
  `reaction` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Zrzut danych tabeli `meme_likes`
--

INSERT INTO `meme_likes` (`like_id`, `m_id`, `u_id`, `reaction`) VALUES
(1, 12, 6, 1),
(2, 12, 7, 1),
(3, 11, 1, 0),
(5, 11, 6, 1),
(6, 10, 6, 1),
(7, 9, 6, 1),
(8, 8, 6, 1),
(9, 7, 6, 1),
(10, 6, 6, 1),
(11, 5, 6, 1),
(12, 4, 6, 1),
(13, 3, 6, 1),
(14, 2, 6, 1),
(15, 1, 6, 1),
(18, 8, 7, 0);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `messages`
--

CREATE TABLE `messages` (
  `msg_id` int NOT NULL,
  `from_id` int NOT NULL,
  `to_id` int NOT NULL,
  `contents` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `u_id` int NOT NULL,
  `username` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `password` varchar(150) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`u_id`, `username`, `password`) VALUES
(1, 'coolUsername', 'a'),
(3, 'yoMama', 'r'),
(5, 'r', 'r'),
(6, 'q', '$2y$10$apd/ZuH4VmtxE2V4KQie6eHP1rTCzUgrTpLoxuFXKoqCHI4z7JEYi'),
(7, 'wwww', '$2y$10$FbY3P6CR8ynQgC3OVcun3ua59Sni3pujfn.NfzYmeSHeuHpmNGUn2'),
(8, 'bestUser', '$2y$10$FbY3P6CR8ynQgC3OVcun3ua59Sni3pujfn.NfzYmeSHeuHpmNGUn2');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user_preferences`
--

CREATE TABLE `user_preferences` (
  `pref_id` int NOT NULL,
  `u_id` int NOT NULL,
  `cat_id` int NOT NULL,
  `score` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`cat_id`);

--
-- Indeksy dla tabeli `memes`
--
ALTER TABLE `memes`
  ADD PRIMARY KEY (`m_id`),
  ADD KEY `cat_id` (`cat_id`),
  ADD KEY `u_id` (`u_id`);

--
-- Indeksy dla tabeli `meme_likes`
--
ALTER TABLE `meme_likes`
  ADD PRIMARY KEY (`like_id`),
  ADD KEY `m_id` (`m_id`),
  ADD KEY `u_id` (`u_id`);

--
-- Indeksy dla tabeli `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`msg_id`),
  ADD KEY `fromid` (`from_id`),
  ADD KEY `toid` (`to_id`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`u_id`);

--
-- Indeksy dla tabeli `user_preferences`
--
ALTER TABLE `user_preferences`
  ADD PRIMARY KEY (`pref_id`),
  ADD KEY `u_id` (`u_id`),
  ADD KEY `cat_id2` (`cat_id`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `categories`
--
ALTER TABLE `categories`
  MODIFY `cat_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT dla tabeli `memes`
--
ALTER TABLE `memes`
  MODIFY `m_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT dla tabeli `meme_likes`
--
ALTER TABLE `meme_likes`
  MODIFY `like_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT dla tabeli `messages`
--
ALTER TABLE `messages`
  MODIFY `msg_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `u_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT dla tabeli `user_preferences`
--
ALTER TABLE `user_preferences`
  MODIFY `pref_id` int NOT NULL AUTO_INCREMENT;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `memes`
--
ALTER TABLE `memes`
  ADD CONSTRAINT `cat_id` FOREIGN KEY (`cat_id`) REFERENCES `categories` (`cat_id`);

--
-- Ograniczenia dla tabeli `meme_likes`
--
ALTER TABLE `meme_likes`
  ADD CONSTRAINT `meme_likes_ibfk_1` FOREIGN KEY (`u_id`) REFERENCES `users` (`u_id`);

--
-- Ograniczenia dla tabeli `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `fromid` FOREIGN KEY (`from_id`) REFERENCES `users` (`u_id`),
  ADD CONSTRAINT `toid` FOREIGN KEY (`to_id`) REFERENCES `users` (`u_id`);

--
-- Ograniczenia dla tabeli `user_preferences`
--
ALTER TABLE `user_preferences`
  ADD CONSTRAINT `cat_id2` FOREIGN KEY (`cat_id`) REFERENCES `categories` (`cat_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
