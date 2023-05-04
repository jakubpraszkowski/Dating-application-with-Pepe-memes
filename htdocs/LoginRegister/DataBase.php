<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function logIn($table, $username, $password)
    {
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where username = '" . $username . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbusername = $row['username'];
            $dbpassword = $row['password'];
            if ($dbusername == $username && password_verify($password, $dbpassword)) {
                $login = true;
            } else $login = false;
        } else $login = false;

        return $login;
    }

    function signUp($table, $username, $password)
    {
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $password = password_hash($password, PASSWORD_DEFAULT);
        $this->sql =
            "INSERT INTO " . $table . " (username, password) VALUES ('" . $username . "','" . $password . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

    function checkMemesInCategory($cat_id)
    {
        if($cat_id == 0)
        {
            $this->sql = "SELECT COUNT(m_id) FROM memes";
            $result = mysqli_query($this->connect, $this->sql);
            $row = mysqli_fetch_assoc($result);
            $count = $row['COUNT(m_id)'];
            return $count;
        }
        else
        {
            $cat_id = $this->prepareData($cat_id);
            $this->sql = "SELECT COUNT(m_id) FROM memes WHERE cat_id = '" . $cat_id . "'";
            $result = mysqli_query($this->connect, $this->sql);
            $row = mysqli_fetch_assoc($result);
            $count = $row['COUNT(m_id)'];
            return $count;
        }
    }
    function getNewMeme($cat_id, $lastMemeId)
    {
        if($cat_id == 0)
        {
            $lastMemeId = $this->prepareData($lastMemeId);
            $this->sql = "SELECT m.m_id, m.url, m.title, c.title AS cat_title, m.u_id, u.username, add_date
            FROM memes m
            LEFT JOIN categories c
            ON m.cat_id=c.cat_id
            LEFT JOIN users u
            ON m.u_id=u.u_id
            WHERE m.m_id < " . $lastMemeId . " ORDER BY m_id DESC LIMIT 1";
            $result = mysqli_query($this->connect, $this->sql);
            $row = mysqli_fetch_assoc($result);
            $myJSON = json_encode($row);
            return $myJSON;
        }
        else
        {
            $cat_id = $this->prepareData($cat_id);
            $lastMemeId = $this->prepareData($lastMemeId);
            $this->sql = "SELECT m.m_id, m.url, m.title, c.title AS cat_title, m.u_id, u.username, add_date
            FROM memes m
            LEFT JOIN categories c
            ON m.cat_id=c.cat_id
            LEFT JOIN users u
            ON m.u_id=u.u_id WHERE m.cat_id = '" . $cat_id . "' AND m.m_id < " . $lastMemeId . " ORDER BY m_id DESC LIMIT 1";
            $result = mysqli_query($this->connect, $this->sql);
            $row = mysqli_fetch_assoc($result);
            $myJSON = json_encode($row);
            return $myJSON;
        }
    }
    function getLatestMeme($cat_id)
    {
        if($cat_id == 0)
        {
            $this->sql = "SELECT m.m_id, m.url, m.title, c.title AS cat_title, m.u_id, u.username, add_date
            FROM memes m
            LEFT JOIN categories c
            ON m.cat_id=c.cat_id
            LEFT JOIN users u
            ON m.u_id=u.u_id
            ORDER BY m_id DESC LIMIT 1";
            $result = mysqli_query($this->connect, $this->sql);
            $row = mysqli_fetch_assoc($result);
            $myJSON = json_encode($row);
            return $myJSON;
        }
        else
        {
            $cat_id = $this->prepareData($cat_id);
            $this->sql = "SELECT m.m_id, m.url, m.title, c.title AS cat_title, m.u_id, u.username, add_date
            FROM memes m
            LEFT JOIN categories c
            ON m.cat_id=c.cat_id
            LEFT JOIN users u
            ON m.u_id=u.u_id WHERE m.cat_id = '" . $cat_id . "' ORDER BY m_id DESC LIMIT 1";
            $result = mysqli_query($this->connect, $this->sql);
            $row = mysqli_fetch_assoc($result);
            $myJSON = json_encode($row);
            return $myJSON;
        }
    }
    function getMemeReactions($m_id, $u_id)
    {
        $m_id = $this->prepareData($m_id);
        $this->sql = "SELECT COUNT(like_id) AS likes FROM meme_likes WHERE m_id = '" . $m_id . "' AND reaction = 1;";
        $result = mysqli_query($this->connect, $this->sql);
        $meme_likes = mysqli_fetch_assoc($result);

        $this->sql = "SELECT COUNT(like_id) AS dislikes FROM meme_likes WHERE m_id = '" . $m_id . "' AND reaction = 0;";
        $result = mysqli_query($this->connect, $this->sql);
        $meme_dislikes = mysqli_fetch_assoc($result);

        $u_id = $this->prepareData($u_id);
        $this->sql = "SELECT reaction FROM meme_likes WHERE u_id = '" . $u_id . "' AND m_id = '" . $m_id . "' ";
        $result = mysqli_query($this->connect, $this->sql);
        $user_reaction = mysqli_fetch_assoc($result);
        if(is_null($user_reaction)){
            $user_reaction = array("reaction" => "5");
        }

        $result = array_merge($meme_likes, $meme_dislikes, $user_reaction);
        $myJSON = json_encode($result);
        return $myJSON;
    }
}
?>