import React from 'react';

const Layout = ({ children }: { children: React.ReactNode }) => {
  return (
    <div className="">
      <main className="bg-gray-200 min-h-screen">
        {children}
      </main>
    </div>
  );
};

export default Layout;
