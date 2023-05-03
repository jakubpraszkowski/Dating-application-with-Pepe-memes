-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 04 Maj 2023, 00:54
-- Wersja serwera: 10.4.25-MariaDB
-- Wersja PHP: 8.1.10

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
  `cat_id` int(11) NOT NULL,
  `title` varchar(40) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Zrzut danych tabeli `categories`
--

INSERT INTO `categories` (`cat_id`, `title`) VALUES
(1, 'IT'),
(2, 'cat2');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `memes`
--

CREATE TABLE `memes` (
  `m_id` int(11) NOT NULL,
  `url` text COLLATE utf8_bin NOT NULL,
  `title` text COLLATE utf8_bin NOT NULL,
  `cat_id` int(11) NOT NULL,
  `u_id` int(11) NOT NULL,
  `add_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Zrzut danych tabeli `memes`
--

INSERT INTO `memes` (`m_id`, `url`, `title`, `cat_id`, `u_id`, `add_date`) VALUES
(1, 'https://static.demilked.com/wp-content/uploads/2021/07/60ed37b2415f9-it-rage-comics-memes-reddit-60e6e9004b503__700.jpg', 'some title', 1, 2, '2023-05-01'),
(2, 'https://s7280.pcdn.co/wp-content/uploads/2019/07/IT-Manager-Pro-Tip-Meme-300x288.jpg.optimal.jpg', 'nice meme', 1, 1, '2023-05-01'),
(3, 'https://miro.medium.com/v2/resize:fit:1400/0*z1mm6izqSeDiKukb', 'xdddd', 1, 3, '2023-05-16'),
(4, 'https://pbs.twimg.com/media/EdXGQguXgAA6VlX.jpg', 'nice meme', 1, 2, '2023-05-26'),
(5, 'https://i.redd.it/h123vo12rof81.jpg', 'hiiiiii', 1, 1, '2023-05-06'),
(6, 'https://programmerhumor.io/wp-content/uploads/2023/02/programmerhumor-io-programming-memes-1303766a3da9d2a-758x746.png', 'UwU', 1, 3, '2023-05-09'),
(7, 'https://i.pinimg.com/originals/c8/dd/1e/c8dd1ef7cfccc936ef24d587f24724eb.jpg', 'that so funny', 2, 1, '2023-05-03'),
(8, 'https://media.discordapp.net/attachments/452923852767363072/1101804189656563712/3404e6d65984292c08e06e9b08fef9ec9ed718dd01c6e93e2e17c96342a9ee0b_1.jpg?width=469&height=662', 'true', 2, 2, '2023-05-03'),
(9, 'https://media.discordapp.net/attachments/781211828314964028/1097981219712536606/02c4e48c744215fdbfd36dfc302ceb7b.png', 'another title', 2, 1, '2023-05-03'),
(10, 'https://media.discordapp.net/attachments/781211828314964028/1099016745525723147/FB_IMG_1652561563741.jpg', 'omg', 2, 1, '2023-05-03'),
(11, 'https://media.discordapp.net/attachments/781211828314964028/1101466966398607370/image0.jpg', 'funny', 2, 2, '2023-05-03'),
(12, 'https://media.discordapp.net/attachments/452923852767363072/1103395267279077466/20230503_094953.png', 'cringe', 2, 3, '2023-05-03');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `messages`
--

CREATE TABLE `messages` (
  `msg_id` int(11) NOT NULL,
  `from_id` int(11) NOT NULL,
  `to_id` int(11) NOT NULL,
  `contents` text COLLATE utf8_bin NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `u_id` int(11) NOT NULL,
  `name` varchar(40) COLLATE utf8_bin NOT NULL,
  `lastname` varchar(40) COLLATE utf8_bin NOT NULL,
  `login` varchar(40) COLLATE utf8_bin NOT NULL,
  `pass` varchar(150) COLLATE utf8_bin NOT NULL,
  `birthday` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`u_id`, `name`, `lastname`, `login`, `pass`, `birthday`) VALUES
(1, 'coolUser', 'idk', 'a', 'a', '2023-04-03'),
(2, 'anotherUser', 'idk2', 'q', 'q', '2023-04-03'),
(3, 'bestUser', 'r', 'r', 'r', '2023-05-17'),
(4, 'anotherUser', 'idk2', 'q', 'q', '2023-04-03'),
(5, 'bestUser', 'r', 'r', 'r', '2023-05-17');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user_preferences`
--

CREATE TABLE `user_preferences` (
  `pref_id` int(11) NOT NULL,
  `u_id` int(11) NOT NULL,
  `cat_id` int(11) NOT NULL,
  `score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  MODIFY `cat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT dla tabeli `memes`
--
ALTER TABLE `memes`
  MODIFY `m_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT dla tabeli `messages`
--
ALTER TABLE `messages`
  MODIFY `msg_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `u_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT dla tabeli `user_preferences`
--
ALTER TABLE `user_preferences`
  MODIFY `pref_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `memes`
--
ALTER TABLE `memes`
  ADD CONSTRAINT `cat_id` FOREIGN KEY (`cat_id`) REFERENCES `categories` (`cat_id`);

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
