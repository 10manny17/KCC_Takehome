"use client";

import { useEffect, useState } from "react";
import Image from "next/image";

interface HurricaneEvent {
  hurricaneName: string;
  year: string;
}

export default function Page() {
  const API_BASE_URL: string | undefined = process.env.NEXT_PUBLIC_API_BASE_URL;
  const [fetchedData, setFetchedData] = useState<HurricaneEvent[]>([]);

  useEffect(() => {
    if (!API_BASE_URL) {
      console.error("API_BASE_URL is not defined.");
      return;
    }

    const fetchData = async () => {
      try {
        const response = await fetch(`${API_BASE_URL}/api/fetchfilterdData`);
        if (!response.ok) {
          throw new Error("Error occurred while fetching data to UI layer");
        }
        const data: HurricaneEvent[] = await response.json();
        setFetchedData(data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  }, [API_BASE_URL]);



  return (
    <>
      <div
        className="container"
        style={{
          width: "100vw",
          height: "100vh",
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          margin: "0",
        }}
      >
        <header
          className="headerSection"
          style={{
            backgroundColor: "#212121",
            margin: "auto",
            textAlign: "center",
            width: "100vw",
            height: "15vh",
            minHeight: "100px",
            color: "white",
            display: "flex",
            flexDirection: "column",
          }}
        >
          <div
            className="headerTopSection"
            style={{
              padding: "10px",
            }}
          >
            <Image
              src="/KCC_logo.jpeg"
              alt="KCC Logo"
              width={50}
              height={50}
            />
          </div>

          <div
            className="headerBottomSection"
            style={{
              padding: "10px",
            }}
          >
            <p>Karen Clark & Company | Hurricane Report Assignment</p>
          </div>
        </header>

        <div
          className="mainSection"
          style={{
            backgroundColor: "#1D3D6F",
            margin: "auto",
            color: "white",
            width: "100vw",
            flexGrow: "1",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <div
            className="displayDataSection"
            style={{
              width: "80vw",
              height: "500px",
              margin: "auto",
              backgroundColor: "white",
              boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.1)",
              borderRadius: "5px",
              overflowY: "auto" as const, 
            }}
          >
            <table
              className="displayDataTable"
              style={{
                color: "black",
                width: "100%",
                borderCollapse: "collapse",
              }}
            >
              <thead>
                <tr>
                  <th
                    style={{
                      padding: "12px",
                      border: "1px solid #ddd",
                      textAlign: "center",
                      backgroundColor: "#f4f4f4",
                      fontWeight: "bold",
                    }}
                  >
                    Name
                  </th>
                  <th
                    style={{
                      padding: "12px",
                      border: "1px solid #ddd",
                      textAlign: "center",
                      backgroundColor: "#f4f4f4",
                      fontWeight: "bold",
                    }}
                  >
                    Date
                  </th>
                </tr>
              </thead>
              <tbody>
                {fetchedData.map((item, index) => (
                  <tr key={index}>
                    <td
                      style={{
                        padding: "12px",
                        border: "1px solid #ddd",
                        textAlign: "center",
                      }}
                    >
                      {item.hurricaneName}
                    </td>
                    <td
                      style={{
                        padding: "12px",
                        border: "1px solid #ddd",
                        textAlign: "center",
                      }}
                    >
                      {item.year}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <div className="buttonRowSection">
            <a href={`${API_BASE_URL}/api/downloadReport`} target="_blank" rel="noopener noreferrer">
              <button>Download Report</button>
            </a>
            <a href={`${API_BASE_URL}/api/viewReport`} target="_blank" rel="noopener noreferrer">
              <button>View Report</button>
            </a>
          </div>
        </div>

        <footer style={{
          width: "100vw",
          height: "5vh",
          padding: "12px",
          textAlign: "center",
          color: "white",
          fontWeight: "bold",
          backgroundColor: "#212121"
        }}>
          <p>Prepared by: Emmanuel C.</p>
        </footer>
      </div>
    </>
  );
}