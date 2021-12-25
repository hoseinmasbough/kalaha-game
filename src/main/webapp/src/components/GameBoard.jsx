import * as React from 'react';
import { Row, Col, Button } from 'antd';
import Paper from './Paper';
import Pit from './Pit';
import Player from './Player';

function GameBoard({ onMove, onReset, data }) {
  const renderPits = (pits, isActive) => {
    return pits.map((pit) => (
      <Pit
        label={pit.capacity}
        key={pit.index}
        onClick={() => onMove(pit.index)}
        disabled={!isActive}
      />
    ));
  };

  const {pits, activePlayer} = data;
  return (
    <Paper>
      <Row gutter={16}>
        <Col xs={24} sm={4}>
          <Player
            name={'PLAYER_1'}
            score={pits[6].capacity}
            hasTurn={activePlayer == 'PLAYER_1'}
          />
        </Col>
        <Col span={16} xs={24} sm={16}>
          <div className="pits-row pits-row-revers">{renderPits(pits.slice(0,6), activePlayer== 'PLAYER_1')}</div>
          <div className={`pits-row`}>
            {renderPits(pits.slice(7,13), activePlayer == 'PLAYER_2')}
          </div>
        </Col>
        <Col span={4} xs={24} sm={4}>
          <Player
            name={'PLAYER_2'}
            score={pits[13].capacity}
            hasTurn={activePlayer== 'PLAYER_2'}
          />
        </Col>
      </Row>
      <Button type="primary" onClick={onReset} block>
        Reset Game
      </Button>
    </Paper>
  );
}

export default GameBoard;
