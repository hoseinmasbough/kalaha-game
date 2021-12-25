import * as React from 'react';
import { Button } from 'antd';

function Pit({ label, onClick, disabled }) {
  return (
    <Button
      shape={'circle'}
      disabled={disabled}
      type="primary"
      className="pit"
      onClick={onClick}
    >
      {label}
    </Button>
  );
}

export default Pit;
