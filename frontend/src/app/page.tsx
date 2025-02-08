"use client";

import { useEffect, useState } from "react";
import Image from "next/image";

// Interface representing a hurricane event data structure
interface HurricaneEvent {
  hurricaneName: string;
  year: string;
}

/**
 * Main Page component for displaying hurricane event data.
 * Fetches hurricane records from an API and displays them in a table format.
 * Provides options to download or view the hurricane report.
 */
export default function Page() {

  // Base URL for API requests, retrieved from environment variables
  const API_BASE_URL: string | undefined = process.env.NEXT_PUBLIC_API_BASE_URL;

  // State to store fetched hurricane event data
  const [fetchedData, setFetchedData] = useState<HurricaneEvent[]>([]);

  /**
  * useEffect hook to fetch hurricane event data from the API on component mount.
  * Logs an error if the API base URL is not defined.
  */
  useEffect(() => {
    if (!API_BASE_URL) {
      console.error("API_BASE_URL is not defined.");
      return;
    }

    /**
       * Fetches hurricane event data from the API.
       * Handles errors in case of a failed network request.
       */
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
      {/* Page Container */}
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
        {/* Header  Section */}
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
            <p> Karen Clark & Company | Hurricane Report Assignment </p>
          </div>
        </header>
        {/* Main Site Section */}

        <div
          className="mainSection"
          style={{
            backgroundColor: "#1D3D6F",
            margin: "auto",
            color: "white",
            width: "100vw",
            flexGrow: "1",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          {/* Table to display Hurricane Data Section */}

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
          {/* Button Row Section */}
          <div className="buttonRowSection">
            {/* Trigger Download API  */}
            <a href={`${API_BASE_URL}/api/downloadReport`} target="_blank" rel="noopener noreferrer">
              <button>Download Report</button>
            </a>
            {/* Trigger View API  */}
            <a href={`${API_BASE_URL}/api/viewReport`} target="_blank" rel="noopener noreferrer">
              <button>View Report</button>
            </a>
          </div>
        </div>

        {/* Footer Section */}
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