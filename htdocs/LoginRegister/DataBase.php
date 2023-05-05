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
	protected $password2;
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
        $password2 = password_hash($password, PASSWORD_DEFAULT);
		$this->sql1 = "SELECT * FROM " . $table . " WHERE `username` = '" . $username . "'";
		$result = mysqli_query($this->connect, $this->sql1); 
		if (mysqli_num_rows($result) > 0) {
			return false;
		}
		if ((($this->verifyLogin($username))==true) && ($this->verifyPassword($password))==true){			
			$this->sql2 = "INSERT INTO " . $table . " (username, password) VALUES ('" . $username . "','" . $password2 . "')";
			if (mysqli_query($this->connect, $this->sql2)) {
				return true;
			} else return false;
		}else return false;
    }
	
	private function verifyLogin($username){
        $regex  = '/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ0-9]*$/';
        if(preg_match($regex, $username) && strlen($username)>=5 && strlen($username)<=25){
            return true;
        }
        else return false;
    }
	
	private function verifyPassword($password){
        if(strlen($password) < 8){
            return false;
        }
        if(strlen($password) > 25){
            return false;
        }
        return true;
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
	function getUserID($username)
    {
		$this->sql = "SELECT u_id
		FROM users 
		WHERE username = '" . $username"'";
		$result = mysqli_query($this->connect, $this->sql);
		$row = mysqli_fetch_assoc($result);
		$userID = json_encode($row);
		return $userID;
    }
}

?>