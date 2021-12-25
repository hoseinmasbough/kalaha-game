import * as React from 'react';
import { Typography } from 'antd';
import Paper from './Paper';

const { Text } = Typography;

function Score({ value = 0, playerName }) {
  return (
    <Paper className='score' >
      <Text type="secondary" ellipsis>
        {playerName}
      </Text>
      <Text>{value}</Text>
    </Paper>
  );
}

export default Score;
