import React from 'react';

function Paper({className = '', children}){
  return <div className={`paper ${className}`}>{children}</div>;
}

export default Paper;
