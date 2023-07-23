import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import LocationForm from './LocationForm';
import TriangleForm from './TriangleForm';
import { useState } from 'react';

function App() {
    const [selectedExample, setSelectedExample] = useState("location");

    function SelectedExample() {
        switch (selectedExample) {
          case "location":
              return (<LocationForm />);
          case "triangle":
              return (<TriangleForm />);
        }
    }

    return (<>
                <Box sx={{ p: 2, border: '1px solid primary.dark' }}  variant="button">
                    <Button variant="outlined" sx={{ m: 1 }} onClick={() => setSelectedExample("location")}>Location example</Button>
                    <Button variant="outlined" sx={{ m: 1 }} onClick={() => setSelectedExample("triangle")}>Triangle example</Button>
                </Box>
                <Box sx={{ p: 2 }}>
                    <SelectedExample />
                </Box>
            </>);
}

export default App;