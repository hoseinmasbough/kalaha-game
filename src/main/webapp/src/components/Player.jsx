import React from 'react';
import UserAvatar from './UserAvatar';
import Score from './Score';

function Player({ score, hasTurn, name }) {
  return (
    <>
      <UserAvatar isActive={hasTurn} />
      <Score value={score} playerName={name} />
    </>
  );
}

export default Player;
