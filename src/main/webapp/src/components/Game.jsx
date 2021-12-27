import React, { useState, useEffect } from 'react';
import { Typography, notification, Icon, Modal, Spin } from 'antd';
import axios from 'axios';
import GameBoard from './GameBoard';
import { initData, secondStep } from '../fake';

const { Title } = Typography;

const baseURL = '/api/kalaha/';

function Game() {
  const [gameState, setGameState] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const initializeGame = async () => {
      try {
        const response = await axios.post(baseURL);
        const data = response.data;
        setGameState(data);
        setLoading(false);
      } catch (error) {
        alert(`Something went wrong, please refresh the page!!`);
      }
    };

    initializeGame();
  }, []);

  const handleReset = async () => {
    try {
      const response = await axios.post(baseURL);
      const data = response.data;
      setGameState(data);
      setLoading(false);
    } catch (error) {
      alert(`Something went wrong, please refresh the page!!`);
    }
  };

  const handleMove = async (pitIndex) => {
    console.log({ pitIndex });
    const url = baseURL + `${gameState.id}/pits/${pitIndex}`;
    setLoading(true);
    try {
      const response = await axios.put(url);
      const data = response.data;
      setGameState(data);
    } catch (error) {
      openNotification('ERROR:', error.details[0]);
    } finally {
      setLoading(false);
    }
  };

  const showWinner = () => {
    Modal.info({
      title: 'Game Result:',
      content: (
        <div>
          <p>Winner is: {gameState.winner}</p>
        </div>
      ),
      onOk: handleReset,
    });
  };

  const openNotification = (title, msg) => {
    notification.open({
      message: title,
      description: msg,
      duration:2.5,
      icon: <Icon type="smile" style={{ color: '#108ee9' }} />,
    });
  };

  useEffect(() => {
    if(loading) return;
    if (gameState.winner) {
      return showWinner();
    }

    gameState.reward && openNotification('Congratulation', gameState.reward);
  
  }, [loading]);

  return (
    <Spin spinning={loading}>
      <Title level={2} style={{ textAlign: 'center' }}>
        Welcome to Kahala Game
      </Title>
      {gameState.pits && (
        <GameBoard onReset={handleReset} onMove={handleMove} data={gameState} />
      )}
    </Spin>
  );
}

export default Game;
