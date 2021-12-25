import React from 'react';
import { Avatar } from 'antd';

function UserAvatar({ isActive }) {
  return (
    <Avatar className={`user-avatar ${isActive && 'active'}`} icon="user" />
  );
}

export default UserAvatar;
