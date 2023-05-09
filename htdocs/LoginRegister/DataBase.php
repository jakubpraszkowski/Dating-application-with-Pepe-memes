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
        $this->sql = "select * from users where username = '" . $username . "'";
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
		$this->sql1 = "SELECT * FROM users WHERE `username` = '" . $username . "'";
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
        $regex  = '/^[A-Za-zĂ„Ä…Ă„ËťĂ„Ä…ÄąĹşÄ‚â€žĂ˘â‚¬Ë‡Ă„Ä…Ă˘â‚¬ĹľĂ„â€šÄąâ€šĂ„Ä…Ă˘â‚¬ĹˇÄ‚â€žĂ˘â€žËÄ‚â€žĂ˘â‚¬Â¦Ă„Ä…Ă˘â‚¬ĹźĂ„Ä…Ă‚Â»Ă„Ä…Ă„â€¦Ä‚â€žĂ˘â‚¬Â Ä‚â€žĂ˘â‚¬ĹľĂ„Ä…ÄąË‡Ä‚â€žĂ‚ÂĂ„Ä…Ă‚ÂĂ„â€šĂ˘â‚¬Ĺ›Ă„Ä…Ă‚Â0-9]*$/';
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
    function getLatestMeme($cat_id, $u_id)
    {
        $cat_id = $this->prepareData($cat_id);
        $u_id = $this->prepareData($u_id);
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
            $meme = mysqli_fetch_assoc($result);

            $lastMemeId = $meme["m_id"];

            $this->sql = "SELECT COUNT(like_id) AS likes FROM meme_likes WHERE m_id = '" . $meme['m_id'] . "' AND reaction = 1;";
            $result = mysqli_query($this->connect, $this->sql);
            $meme_likes = mysqli_fetch_assoc($result);
    
            $this->sql = "SELECT COUNT(like_id) AS dislikes FROM meme_likes WHERE m_id = '" . $meme['m_id'] . "' AND reaction = -1;";
            $result = mysqli_query($this->connect, $this->sql);
            $meme_dislikes = mysqli_fetch_assoc($result);
    
            $this->sql = "SELECT reaction FROM meme_likes WHERE u_id = '" . $u_id . "' AND m_id = '" . $meme['m_id'] . "' ";
            $result = mysqli_query($this->connect, $this->sql);
            $user_reaction = mysqli_fetch_assoc($result);
            if(is_null($user_reaction)){
                $user_reaction = array("reaction" => "5");
            }
    
            $result = array_merge($meme, $meme_likes, $meme_dislikes, $user_reaction);
            $myJSON = json_encode($result);
            return $myJSON;
        }
        else
        {
            $this->sql = "SELECT m.m_id, m.url, m.title, c.title AS cat_title, m.u_id, u.username, add_date
            FROM memes m
            LEFT JOIN categories c
            ON m.cat_id=c.cat_id
            LEFT JOIN users u
            ON m.u_id=u.u_id WHERE m.cat_id = '" . $cat_id . "' ORDER BY m_id DESC LIMIT 1";
            $result = mysqli_query($this->connect, $this->sql);
            $meme = mysqli_fetch_assoc($result);

            $lastMemeId = $meme["m_id"];

            $this->sql = "SELECT COUNT(like_id) AS likes FROM meme_likes WHERE m_id = '" . $meme['m_id'] . "' AND reaction = 1;";
            $result = mysqli_query($this->connect, $this->sql);
            $meme_likes = mysqli_fetch_assoc($result);
    
            $this->sql = "SELECT COUNT(like_id) AS dislikes FROM meme_likes WHERE m_id = '" . $meme['m_id'] . "' AND reaction = -1;";
            $result = mysqli_query($this->connect, $this->sql);
            $meme_dislikes = mysqli_fetch_assoc($result);
    
            $this->sql = "SELECT reaction FROM meme_likes WHERE u_id = '" . $u_id . "' AND m_id = '" . $meme['m_id'] . "' ";
            $result = mysqli_query($this->connect, $this->sql);
            $user_reaction = mysqli_fetch_assoc($result);
            if(is_null($user_reaction)){
                $user_reaction = array("reaction" => "5");
            }
    
            $result = array_merge($meme, $meme_likes, $meme_dislikes, $user_reaction);
            $myJSON = json_encode($result);
            return $myJSON;
        }
	}
	function addReaction($m_id, $u_id, $reaction)
    {
        $m_id = $this->prepareData($m_id);
        $u_id = $this->prepareData($u_id);
        $reaction = $this->prepareData($reaction);
		$this->sql = "INSERT INTO meme_likes(m_id, u_id, reaction)
        VALUES('" . $m_id . "', '" . $u_id . "', '" . $reaction . "')";
		$result = mysqli_query($this->connect, $this->sql);
		return $result;
    }
    function removeReaction($m_id, $u_id)
    {
        $m_id = $this->prepareData($m_id);
        $u_id = $this->prepareData($u_id);
		$this->sql = "DELETE FROM meme_likes WHERE u_id = '" . $u_id . "' AND m_id = '" . $m_id . "' ";
		$result = mysqli_query($this->connect, $this->sql);
		return $result;
    }
    function getUserID($username)
    {
		$this->sql = "SELECT u_id
		FROM users 
		WHERE username = '" . $username . "'";
		$result = mysqli_query($this->connect, $this->sql);
		$row = mysqli_fetch_assoc($result);
		$userID = $row['u_id'];
        return $userID;
    }
	
	function getUserProfile($u_id)
    {
        $u_id = $this->prepareData($u_id);

        $this->sql = "SELECT u.username, c.title, IFNULL(SUM(ml.reaction), 0) AS points 
        FROM categories c
        JOIN users u ON u_id = ".$u_id."
        LEFT JOIN memes m ON m.cat_id = c.cat_id
        LEFT JOIN meme_likes ml ON m.m_id = ml.m_id AND u.u_id = ml.u_id 
        GROUP BY u.username, c.title;";
        $data = array();
        $result = mysqli_query($this->connect, $this->sql);

        if($result->num_rows != 0)
        {
            while ($row = mysqli_fetch_assoc($result)) {
                $data[] = $row;
            }
        }
        else {
            $this->sql = "SELECT username FROM users WHERE u_id = ".$u_id.";";
        
            $result = mysqli_query($this->connect, $this->sql);

            $row = mysqli_fetch_assoc($result);
            $data[] = $row;
        }
        
        return json_encode($data);
    }
    function getNewMemes($u_id, $cat_id, $lastMemeId, $count)
    {
        $u_id = $this->prepareData($u_id);
        $cat_id = $this->prepareData($cat_id);
        $lastMemeId = $this->prepareData($lastMemeId);
        $array= array();

        if($cat_id == 0)
        {
            for($i = $count; $i > 0; $i--){
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
                if(is_null($row)){return json_encode($array);}
                $lastMemeId = $row["m_id"];

                $this->sql = "SELECT COUNT(like_id) AS likes FROM meme_likes WHERE m_id = '" . $row['m_id'] . "' AND reaction = 1;";
                $result = mysqli_query($this->connect, $this->sql);
                $meme_likes = mysqli_fetch_assoc($result);
        
                $this->sql = "SELECT COUNT(like_id) AS dislikes FROM meme_likes WHERE m_id = '" . $row['m_id'] . "' AND reaction = -1;";
                $result = mysqli_query($this->connect, $this->sql);
                $meme_dislikes = mysqli_fetch_assoc($result);
        
                $this->sql = "SELECT reaction FROM meme_likes WHERE u_id = '" . $u_id . "' AND m_id = '" . $row['m_id'] . "' ";
                $result = mysqli_query($this->connect, $this->sql);
                $user_reaction = mysqli_fetch_assoc($result);
                if(is_null($user_reaction)){
                    $user_reaction = array("reaction" => "5");
                }
        
                $result = array_merge($row, $meme_likes, $meme_dislikes, $user_reaction);
                array_push($array, $result);    
            }
            return json_encode($array);
        }
        else
        {
            for($i = $count; $i > 0; $i--){
                
                $this->sql = "SELECT m.m_id, m.url, m.title, c.title AS cat_title, m.u_id, u.username, add_date
                FROM memes m
                LEFT JOIN categories c
                ON m.cat_id=c.cat_id
                LEFT JOIN users u
                ON m.u_id=u.u_id
                WHERE m.cat_id = '" . $cat_id . "' AND m.m_id < " . $lastMemeId . " ORDER BY m_id DESC LIMIT 1";
                $result = mysqli_query($this->connect, $this->sql);
                $row = mysqli_fetch_assoc($result);
                if(is_null($row)){return json_encode($array);}
                $lastMemeId = $row["m_id"];

                $this->sql = "SELECT COUNT(like_id) AS likes FROM meme_likes WHERE m_id = '" . $row['m_id'] . "' AND reaction = 1;";
                $result = mysqli_query($this->connect, $this->sql);
                $meme_likes = mysqli_fetch_assoc($result);
        
                $this->sql = "SELECT COUNT(like_id) AS dislikes FROM meme_likes WHERE m_id = '" . $row['m_id'] . "' AND reaction = -1;";
                $result = mysqli_query($this->connect, $this->sql);
                $meme_dislikes = mysqli_fetch_assoc($result);
                
                $this->sql = "SELECT reaction FROM meme_likes WHERE u_id = '" . $u_id . "' AND m_id = '" . $row['m_id'] . "' ";
                $result = mysqli_query($this->connect, $this->sql);
                $user_reaction = mysqli_fetch_assoc($result);
                if(is_null($user_reaction)){
                    $user_reaction = array("reaction" => "5");
                }
        
                $result = array_merge($row, $meme_likes, $meme_dislikes, $user_reaction);
                array_push($array, $result);    
            }
            return json_encode($array);
        }
    }
}

?>